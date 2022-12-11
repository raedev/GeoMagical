package com.magical.map.core

/**
 * 场景监听器
 * @author RAE
 * @date 2022/12/11
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface MapSceneListener {

    /**
     * 场景发生改变的时候回调
     * @param current 当前场景
     * @param next 新的场景
     */
    fun onMapSceneChanged(current: MapScene?, next: MapScene)


    /**
     * 场景销毁的时候回调
     */
    fun onMapSceneDestroy(scene: MapScene)
}