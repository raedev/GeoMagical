package com.magical.map.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.magical.map.content.MapContext
import com.magical.map.utils.MapLog
import com.magical.map.view.MagicalMapView

/**
 * 地图控制器
 * @author RAE
 * @date 2022/11/01
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
open class MapController(
    /** 绑定生命周期，建议传与Mapview紧密结合的Owner */
    val owner: LifecycleOwner,
    val mapContext: MapContext,
    val mapView: MagicalMapView,
) : LifecycleEventObserver {

    companion object {
        /**
         * 默认的控制器
         */
        private const val DEFAULT_CONTROLLER_NAME = "DefaultMapController"

        /** 控制器缓存 */
        private val controllers: MutableMap<String, MapController> = mutableMapOf()

        /**
         * 获取地图控制器，支持夸Activity调用。
         */
        fun get(name: String = DEFAULT_CONTROLLER_NAME): MapController? {
            return controllers[name]
        }

        /**
         * 添加到全局控制器
         */
        private fun addController(controller: MapController) {
            if (!controllers.containsKey(controller.controllerName)) {
                controllers[controller.controllerName] = controller
            }
        }

        /**
         * 移除全局控制器
         */
        private fun removeController(controller: MapController) {
            controllers.remove(controller.controllerName)
        }
    }

    /** 当前控制器名称，用于区分应用存在多个控制器的情况 */
    var controllerName: String = DEFAULT_CONTROLLER_NAME

    /** 图层管理器 */
    val layerManager: MapLayerManager = MapLayerManager(mapView, mapContext)

    /**
     * 是否为默认的控制器
     */
    val isDefaultController: Boolean
        get() = controllerName == DEFAULT_CONTROLLER_NAME

    init {
        // 控制器关联生命周期
        addController(this)
        mapView.attachController(this)
        owner.lifecycle.addObserver(this)
        MapLog.d("初始化控制器：$this")
    }

    /**
     * 由控制器监听生命周期，分发到图层管理器中，图层管理器再分发到各自的图层中处理。
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        mapView.onStateChanged(source, event)
        // 转发到图层控制器处理
        layerManager.onStateChanged(source, event)
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                // 添加当前控制器
                addController(this)
                // 附着控制器到地图中去
                mapView.attachController(this)
            }
            Lifecycle.Event.ON_STOP -> {
                MapLog.d("停止地图控制器")
                // 注意：STOP时不需要移除当前Controller，只以供其他界面调用
                mapView.detachController()
            }
            Lifecycle.Event.ON_DESTROY -> {
                MapLog.d("销毁地图控制器")
                source.lifecycle.removeObserver(this)
                // 移除当前控制器
                removeController(this)
                // 通知地图销毁
                mapView.destroy()
                // 销毁上下文
                mapContext.destroy()
            }
            else -> return
        }
    }
}