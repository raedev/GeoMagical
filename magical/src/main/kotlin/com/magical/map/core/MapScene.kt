package com.magical.map.core

import android.os.Bundle

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

    /**
     * 场景初始化
     */
    abstract fun onMapSceneCreated(controller: MapController, bundle: Bundle?)

    /**
     * 场景发生变化，当其他场景请求进入时，将调用该方法来决定当前场景是否需要处理，如果返回TRUE，地图场景将被替换，否则维持当前场景。
     */
    abstract fun onMapSceneChanged(controller: MapController, scene: MapScene): Boolean

    /**
     * 退出场景
     */
    abstract fun onMapSceneDestroy(controller: MapController)

    /**
     * 退出场景
     */
    fun exit() {
        sceneController?.let {
            // 退出
            onMapSceneDestroy(it.controller)
            it.listeners.forEach { listener -> listener.onMapSceneDestroy(this) }
            it.currentScene = null
            sceneController = null
        }
    }
}