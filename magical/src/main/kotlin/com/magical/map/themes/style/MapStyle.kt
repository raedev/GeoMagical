package com.magical.map.themes.style

import com.magical.map.themes.symbol.MapSymbol

/**
 * 地图样式抽象类，其中包括：点、线、面样式
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused")
abstract class MapStyle(val name: String) {

    /** 默认的样式 */
    abstract var symbol: MapSymbol

    override fun toString(): String {
        return symbol.toString()
    }

    fun toJson(): String = symbol.toJson()
}