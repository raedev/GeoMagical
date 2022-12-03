package com.magical.map.core

import com.magical.map.layer.MapLayer

/**
 * 地图图层监听器
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface MapLayerListener {

    /**
     * 图层已经添加
     */
    fun onLayerAttached(layer: MapLayer) = Unit

    /**
     * 图层已经移除
     */
    fun onLayerDetached(layer: MapLayer) = Unit
}