package com.magical.map.layer

/**
 * 图层类型
 * @author RAE
 * @date 2022/06/25
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
enum class LayerType(val type: Int) {

    /**
     * 影像底图类型，顺序：tpk -> tile -> wms -> wmts
     */
    Basemap(0),

    /**
     * 系统功能类型，如：GPS、轨迹、涂鸦
     */
    System(1),

    /**
     * 业务功能类型，如：fcs、shp
     */
    Feature(2),

    /**
     * 置顶图层
     */
    Top(9);

    companion object {
        fun fromType(type: Int): LayerType {
            values().forEach {
                if (it.type == type) return it
            }
            return Feature
        }
    }
}