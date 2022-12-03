package com.magical.map.layer

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.magical.map.content.MapContext
import com.magical.map.content.MapResources
import com.magical.map.core.MapLayerManager
import com.magical.map.exception.MapLayerException
import com.magical.map.utils.MapLog
import com.magical.map.view.MagicalMapView
import com.magical.map.view.MapLayerView
import com.magical.map.view.MapMotionEvent
import java.lang.ref.WeakReference

/**
 * 地图图层，所有的图层的父类
 * @author RAE
 * @date 2022/11/01
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class MapLayer : LifecycleEventObserver {


    /** 地图Context */
    private var mMapContext: MapContext? = null

    /** 地图引用 */
    private var mMapViewRef: WeakReference<MagicalMapView>? = null

    /** 图层信息 */
    lateinit var layerInfo: LayerInfo
        internal set

    /** 是否已经附加到地图 */
    private var mIsAttach: Boolean = false

    /** 是否已经从地图中移除 */
    private var mIsDetach: Boolean = false

    /** 图层View */
    var mapLayerView: MapLayerView? = null
        private set

    /** 受保护的子类调用的地图Context */
    protected val mapContext: MapContext
        get() = mMapContext ?: throw NullPointerException("图层尚未加载到地图")

    /** Android Context */
    protected val context: Context
        get() = mapContext.context

    /** 地图资源 */
    protected val resources: MapResources
        get() = mapContext.resources


    /** 图层名称 */
    val name: String
        get() = layerInfo.name

    /** 图层标题 */
    val title: String
        get() = layerInfo.title

    /** 设置图层透明度 */
    open var alpha: Int
        get() = layerInfo.alpha
        set(value) {
            layerInfo.alpha = value
        }

    /** 设置图层可见性 */
    open var visible: Boolean
        get() = layerInfo.visible
        set(value) {
            layerInfo.visible = value
        }

    /** 获取地图 */
    protected val baseMapView: View?
        get() = mMapViewRef?.get()?.getBaseMapView()

    /** 地图 */
    protected val magicalMapView: MagicalMapView?
        get() = mMapViewRef?.get()

    /**
     * 获取原始图层
     */
    abstract fun getOriginLayer(): Any

    /** 图层初始化完毕后运行的方法 */
    var onAttachListener: ((MapLayer) -> Unit)? = null

    protected val layerManager: MapLayerManager?
        get() = magicalMapView?.controller?.layerManager

    // region 由图层管理器内部调用的方法

    /**
     * 准备添加图层时候调用，方法运行在线程中。
     * @param context 地图上下文
     * @param layerInfo 图层信息
     */
    internal fun prepare(
        context: MapContext,
        layerInfo: LayerInfo,
        mapView: MagicalMapView
    ): PrepareLayerResult {
        this.mMapViewRef = WeakReference(mapView)
        this.mMapContext = context
        this.layerInfo = layerInfo
        return onPrepare(context, layerInfo)
    }

    /**
     * 图层添加时附加地图Context时候被调用
     */
    internal fun performAttach() {
        if (mIsAttach) return
        // 初始化默认的图层属性
        this.visible = layerInfo.visible
        this.alpha = layerInfo.alpha
        this.onAttach()
        MapLog.d("图层onAttach：$title")
        // 执行View添加
        this.magicalMapView?.let { view ->
            performCreateLayerView(view, LayoutInflater.from(context))?.let { layout ->
                this.mapLayerView = layout
                view.setLayerView(layout)
                this.onViewCreated(layout)
            }
        }
        this.mIsAttach = true
        this.mIsDetach = false
        // 执行自定义的方法
        onAttachListener?.invoke(this)
    }


    /**
     * 从地图中移除的时候触发
     */
    internal fun performDetach() {
        if (mIsDetach) return
        this.onDetach()
        MapLog.w("图层onDetach：$title")
        this.mapLayerView?.let { view ->
            magicalMapView?.removeLayerView(view)
            this.mapLayerView = null
        }
        this.mMapViewRef?.clear()
        this.mMapViewRef = null
        this.mMapContext = null
        this.mIsAttach = false
        this.mIsDetach = true
        this.onAttachListener = null
    }

    /**
     * 创建视图
     */
    private fun performCreateLayerView(
        container: MagicalMapView,
        layoutInflater: LayoutInflater
    ): MapLayerView? {
        val view = this.onCreateLayerView(container, layoutInflater) ?: return null
        if (view !is MapLayerView) error("地图图层View需使用MapLayerView为根布局")
        return view
    }

    /**
     * 创建图层视图
     */
    protected open fun onCreateLayerView(
        container: MagicalMapView,
        inflater: LayoutInflater
    ): View? = null

    /**
     * 图层视图创建成功
     */
    protected open fun onViewCreated(view: MapLayerView) = Unit

    /**
     * 请求图层独占模式
     */
    internal fun performExclusiveModeChanged(exclusive: Boolean) {
        this.onExclusiveModeChanged(exclusive)
    }


    // endregion

    // region  地图生命周期

    /**
     * 准备图层
     */
    protected open fun onPrepare(context: MapContext, layerInfo: LayerInfo): PrepareLayerResult {
        return PrepareLayerResult.success()
    }

    /**
     * 当图层已经附加到地图中的时候回调
     */
    protected open fun onAttach() = Unit

    /**
     * 当图层从地图中移除的时候回调
     */
    protected open fun onDetach() = Unit

    /**
     * 生命周期重启
     */
    protected open fun onResume() = Unit

    /**
     * 生命周期暂停
     */
    protected open fun onPause() = Unit

    /**
     * 生命周期停止
     */
    protected open fun onStop() = Unit

    /**
     * 生命周期释放
     */
    protected open fun onDestroy() = Unit

    /**
     * Touch事件消费
     */
    protected open fun onTouchEvent(event: MapMotionEvent): Boolean = false

    /**
     * Touch事件消费
     */
    protected open fun onTouchEvent(event: MotionEvent) = Unit

    /**
     * 分发触摸事件
     */
    fun dispatchTouchEvent(event: MapMotionEvent): Boolean {
        return onTouchEvent(event)
    }

    /**
     * 分发触摸事件
     */
    fun dispatchTouchEvent(event: MotionEvent) {
        this.onTouchEvent(event)
    }

    fun dispatchDrawFrame() {
        this.onMapDrawFrame()
    }

    /**
     * 地图渲染的时候回调
     */
    protected open fun onMapDrawFrame() = Unit

    /**
     * 地图缩放层级发生更改的时候回调
     * @param oldLevel 上一次层级
     * @param level 当前层级
     */
    open fun onZoomChanged(oldLevel: Float, level: Float): Boolean = false

    /**
     * 地图生命周期回调，由框架内部调用。
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()
            Lifecycle.Event.ON_STOP -> this.onStop()
            Lifecycle.Event.ON_DESTROY -> {
                this.destroy()
            }
            else -> return
        }
    }

    /**
     * 请求独占模式
     */
    protected open fun onExclusiveModeChanged(exclusive: Boolean) = Unit

    /**
     * 销毁图层
     */
    private fun destroy() {
        this.performDetach()
        this.onDestroy()
    }

    // endregion


    /**
     * 刷新当前图层
     */
    open fun refresh() = Unit

    @Suppress("NOTHING_TO_INLINE")
    @Throws(MapLayerException::class)
    protected inline fun error(message: String): Nothing {
        throw MapLayerException(message)
    }

    protected fun runOnMainThread(runnable: Runnable) {
        this.magicalMapView?.post(runnable)
    }

    override fun toString(): String = layerInfo.toString()

}