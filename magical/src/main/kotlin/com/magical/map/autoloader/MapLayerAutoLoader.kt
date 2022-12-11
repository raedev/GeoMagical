package com.magical.map.autoloader

import androidx.lifecycle.lifecycleScope
import com.magical.location.MagicalLocationManager
import com.magical.map.content.MapContext
import com.magical.map.core.MapController
import com.magical.map.layer.LayerInfo
import com.magical.map.layer.LayerType
import com.magical.map.layer.LocationMapLayer
import com.magical.map.utils.MapLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * 地图图层自动装载器，根据配置文件自动加载图层。
 * 读取优先级：asset配置文件 -> 文件配置 -> 用户本地配置
 * @author RAE
 * @date 2022/11/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */

open class MapLayerAutoLoader(
    protected val controller: MapController,
    protected val params: AutoLoaderParams,
) {

    protected val context: MapContext
        get() = controller.mapContext


    protected open fun onCreateConfigLoader(loader: LayerLoader): LayerLoader {
        return loader
    }

    /**
     * 加载图层
     */
    @Suppress("UselessCallOnNotNull")
    private fun loadLayers(): List<LayerInfo> {
        // 按顺序加载装配器
        val fileLoader =
            LayerFileLoader(context, params.featureExtension, params.basemapExtension)
        val assetsLoader = AssetLayerConfigLoader(fileLoader)
        val configLoader = LayerConfigLoader(assetsLoader)
        // 加载图层
        val layers = onCreateConfigLoader(configLoader).loadLayers()
        // 图层正确性检查
        val result = mutableListOf<LayerInfo>()
        layers.forEach { layer ->
            // 没有图层类处理
            if (layer.clazz.isNullOrBlank()) {
                MapLog.w("没有定义图层的Class处理：$layer")
                return@forEach
            }
            // 本地文件不存在
            layer.path?.let {
                if (!File(it).exists()) {
                    MapLog.w("图层文件不存在：$layer")
                    return@forEach
                }
            }
            when (layer.type) {
                LayerType.Basemap.type, LayerType.Feature.type -> {
                    if (layer.path.isNullOrBlank() && layer.url.isNullOrBlank()) {
                        MapLog.w("图层路径为空：$layer")
                        return@forEach
                    }
                }
            }
            result.add(layer)
        }
        return result
    }


    /**
     * 开始自动装配所有图层
     */
    fun autoload() {
        controller.owner.lifecycleScope.launch(Dispatchers.IO) {
            controller.layerManager.addLayers(loadLayers())
            centerToLocation()
        }
    }

    /**
     * 装载底图
     */
    fun autoloadBasemap() {
        controller.owner.lifecycleScope.launch(Dispatchers.IO) {
            val layers = loadLayers().filter { it.type == LayerType.Basemap.type }
            controller.layerManager.addLayers(layers)
            centerToLocation()
        }
    }

    /**
     * 加载GPS位置图层
     */
    fun <T : LocationMapLayer> loadLocationLayer(clazz: Class<T>) {
        val layer =
            LayerInfo(LocationMapLayer.LAYER_NAME, "GPS位置图层", clazz.name, LayerType.Top.type, 9)
        controller.layerManager.addLayer(layer)
    }

    /**
     * 当前位置居中
     */
    protected fun centerToLocation() {
        MagicalLocationManager.getLastLocation(context.context)?.let {
            controller.mapView.setMapLevel(17f, false)
            controller.mapView.setCenter(it.longitude, it.latitude, false)
        }
    }
}