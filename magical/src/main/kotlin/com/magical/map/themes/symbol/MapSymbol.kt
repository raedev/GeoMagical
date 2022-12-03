package com.magical.map.themes.symbol

import com.magical.map.themes.ThemeGson

/**
 * 地图符号样式
 * @author RAE
 * @date 2022/11/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class MapSymbol(val name: String) {

    override fun toString(): String {
        return "${this::class.java.simpleName}: ${this.toJson()}"
    }

    fun toJson(): String {
        return ThemeGson().toJson(this)
    }
}