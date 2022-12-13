package com.magical.map.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

/**
 * 地图场景
 * @author RAE
 * @date 2022/12/11
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class MapScene(
    /** 场景名称，唯一标志 */
    val name: String,
    /** 场景标题，一般用于提示 */
    val title: String
) {

    internal var sceneController: MapSceneController? = null

    private var viewRef: WeakReference<View>? = null

    protected val view: View?
        get() = viewRef?.get()


    /**
     * 创建视图
     */
    protected open fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): View? = null

    /**
     * 视图已经加载
     */
    protected open fun onViewCreated(view: View) = Unit

    /**
     * 视图销毁
     */
    protected open fun onDestroyView(view: View) = Unit

    internal fun performMapSceneCreated(controller: MapController, bundle: Bundle?) {
        // 创建视图
        val parent = controller.mapView
        val context = parent.context
        view?.let { parent.removeView(it) }
        onCreateView(context, LayoutInflater.from(context), parent)?.let {
            // 添加到视图中去
            parent.addView(it)
            viewRef = WeakReference(it)
        }
        onMapSceneCreated(controller, bundle)
        view?.let { onViewCreated(it) }
    }

    private fun performMapSceneDestroy(controller: MapSceneController) {
        // 先移除View
        view?.let {
            onDestroyView(it)
            controller.controller.mapView.removeView(it)
            viewRef!!.clear()
            viewRef = null
        }
        // 再退出
        onMapSceneDestroy(controller.controller)
        controller.listeners.forEach { listener -> listener.onMapSceneDestroy(this) }
        controller.currentScene = null
        sceneController = null
    }

    /**
     * 场景初始化
     */
    protected abstract fun onMapSceneCreated(controller: MapController, bundle: Bundle?)

    /**
     * 场景发生变化，当其他场景请求进入时，将调用该方法来决定当前场景是否需要处理，如果返回TRUE，地图场景将被替换，否则维持当前场景。
     */
    abstract fun onMapSceneChanged(controller: MapController, scene: MapScene): Boolean

    /**
     * 退出场景
     */
    protected abstract fun onMapSceneDestroy(controller: MapController)

    /**
     * 拦截回退事件
     */
    open fun onBackPressed(context: Context): Boolean {
        exit()
        return true
    }

    /**
     * 退出场景
     */
    fun exit() {
        sceneController?.let { performMapSceneDestroy(it) }
    }
}