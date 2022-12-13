package com.magical.map.core

import android.content.Context
import android.os.Bundle

/**
 * 地图场景展示控制器
 * @author RAE
 * @date 2022/12/11
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MapSceneController(internal val controller: MapController) {


    /** 当前场景，地图只能处理一个场景，当请求多个场景时，应退出当前正在进行的场景。 */
    internal var currentScene: MapScene? = null

    internal val listeners: MutableList<MapSceneListener> = mutableListOf()

    fun addMapSceneListener(listener: MapSceneListener) {
        if (!listeners.contains(listener)) listeners.add(listener)
    }

    fun removeMapSceneListener(listener: MapSceneListener) {
        listeners.remove(listener)
    }

    /**
     * 显示场景
     */
    fun show(scene: MapScene, bundle: Bundle? = null): MapScene {
        val current = currentScene
        if (current != null && !current.onMapSceneChanged(controller, scene)) {
            // 不替换当前场景
            return current
        }
        // 当前场景退出
        current?.let { exitScene(it) }
        // 进入新的场景
        scene.sceneController = this
        scene.performMapSceneCreated(controller, bundle)
        // 通知改变
        listeners.forEach { it.onMapSceneChanged(current, scene) }
        // 更新场景
        currentScene = scene
        return scene
    }

    /**
     * 分发事件
     */
    fun dispatchBackPressedEvent(context: Context): Boolean {
        return currentScene?.onBackPressed(context) == true
    }

    /**
     * 退出当前场景
     */
    fun exit() {
        val scene = currentScene ?: return
        exitScene(scene)
        currentScene = null
    }

    private fun exitScene(scene: MapScene) {
        scene.exit()
        listeners.forEach { it.onMapSceneDestroy(scene) }
    }

    internal fun destroy() {
        exit()
        listeners.clear()
    }
}