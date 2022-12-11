package com.magical.map.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.magical.map.content.MapContext
import com.magical.map.layer.LayerInfo
import com.magical.map.layer.LayerType
import com.magical.map.layer.MapLayer
import com.magical.map.layer.MapLayerList
import com.magical.map.utils.MapLog
import com.magical.map.view.MagicalMapView
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.ScheduledExecutorService

/**
 * 地图图层管理器
 * @author RAE
 * @date 2022/11/01
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused")
@OptIn(DelicateCoroutinesApi::class)
class MapLayerManager(
    private val mapView: MagicalMapView,
    private val context: MapContext
) : LifecycleEventObserver {

    /** 图层列表 */
    val layers: MapLayerList = MapLayerList()

    /** 添加图层调度器：目的是以队列的形式添加图层，即使是批量添加也能保持按顺序添加。 */
    private val mDispatcher: ExecutorCoroutineDispatcher =
        newFixedThreadPoolContext(1, "LayerThreadPool")

    /** 图层的协程作用域 */
    private val mScope = MainScope()

    /** 图层监听 */
    private val mLayerListeners: MutableList<MapLayerListener> = mutableListOf()

    fun addLayerListener(listener: MapLayerListener) {
        if (!mLayerListeners.contains(listener)) mLayerListeners.add(listener)
    }

    fun removeLayerListener(listener: MapLayerListener) {
        mLayerListeners.remove(listener)
    }

    /**
     * 生命周期回调
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        layers.forEach { layer ->
            // 转发到各个图层中去处理
            layer.onStateChanged(source, event)
        }

        // 销毁图层
        if (event == Lifecycle.Event.ON_DESTROY) {
            layers.clear()
            if (mScope.isActive) {
                mScope.cancel("LayerManager is destroy")
            }
            // 结束线程
            (mDispatcher.executor as ScheduledExecutorService).shutdown()
        }
    }

    /**
     * 移除所有图层
     */
    fun clear() = mScope.launch(mDispatcher) {
        layers.forEach { layer ->
            remove(layer)
        }
    }

    /**
     * 批量添加图层
     */
    fun addLayers(layers: List<LayerInfo>) {
        for (layer in layers) {
            addLayer(layer)
        }
    }

    /**
     * 添加图层
     * 先添加到图层列表，然后再添加到地图图层，最后刷新地图重新排序
     */
    fun addLayer(layerInfo: LayerInfo): MapLayer {
        if (layers.contains(layerInfo.name)) {
            MapLog.w("图层已经添加：$layerInfo")
            return layers[layerInfo.name]!!
        }
        // 实例化图层并触发准备动作
        val layer = layerInfo.create()
        this.addLayer(layer)
        return layer
    }

    /**
     * 添加图层，调用该方法时图层必须设置layerInfo
     */
    private fun addLayer(layer: MapLayer) = mScope.launch(mDispatcher) {
        val layerInfo = layer.layerInfo
        MapLog.d("添加图层：$layerInfo")
        val result = layer.prepare(context, layerInfo, mapView)
        if (!result.successfully) {
            layer.onPrepareError()
            MapLog.e("图层准备失败：$result, 图层信息：$layerInfo")
            return@launch
        }
        // 切换到主线程
        launch(Dispatchers.Main) mainThread@{
            MapLog.i("准备附加到图层：$layerInfo")
            layers.addLayer(layer)
            if (mapView.addLayer(layer)) {
                layer.performAttach()
                // 地图添加成功后重新刷新
                mapView.refresh()
                // 通知图层已经添加
                mLayerListeners.forEach { it.onLayerAttached(layer) }
                return@mainThread
            }
            // 地图添加失败的时候移除
            layers.removeLayer(layer)
            MapLog.d("图层添加成功：$layerInfo")
        }.join()
    }

    /**
     * 移除图层
     */
    fun remove(layer: MapLayer) {
        if (mapView.removeLayer(layer)) {
            layer.performDetach()
            layers.removeLayer(layer)
            // 通知图层已经移除
            mLayerListeners.forEach { it.onLayerDetached(layer) }
        }
    }

    /**
     * 获取图层
     */
    fun getLayer(name: String): MapLayer? {
        return this.layers[name]
    }

    /**
     * 获取要素图层
     */
    fun getFeatureLayer(name: String): MapLayer? {
        return layers.firstOrNull { it.name == name && it.layerInfo.type == LayerType.Feature.type }
    }

    /**
     * 根据具体的图层类获取图层集合
     */
    fun <T : MapLayer> getLayers(clazz: Class<T>, name: String? = null): List<MapLayer> {
        return layers.filter { if (name.isNullOrEmpty()) it::class.java == clazz else it::class.java == clazz && it.name == name }
    }

    /**
     * 根据图层类型获取图层
     */
    fun getLayer(type: LayerType, name: String? = null): List<MapLayer> {
        return this.layers.filter { layer ->
            if (name == null)
                layer.layerInfo.type == type.type
            else
                layer.layerInfo.type == type.type && layer.name == name
        }
    }

    /**
     * 请求图层独占模式，也就是只能操作当前图层，禁用其他图层事件
     * @param layer 当前图层
     * @param exclusive 是否独占模式
     */
    fun requestExclusiveLayer(layer: MapLayer, exclusive: Boolean = true) {
        MapLog.d("图层[${layer.name}]请求独占模式=$exclusive")
        layers.forEach {
            if (layer == it) return@forEach
            it.performExclusiveModeChanged(exclusive)
        }
    }

    /**
     * 图层重新排序
     */
    fun resortLayer(layerInfoList: List<LayerInfo>) {
        val map = mutableMapOf<String, Int>()
        layerInfoList.forEach { map[it.name] = it.orderNo }
        layers.forEach {
            if (map.contains(it.name)) {
                it.layerInfo.orderNo = map[it.name]!!
            }
        }
        layers.sort()
        // 通知地图进行排序
        mapView.resortLayers(layers)
    }

}