@file:Suppress("unused")

package com.magical.map.themes

import android.content.Context
import com.magical.map.themes.style.MapStylesheet

/**
 * 地图主题，用来管理地图样式。
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class MapTheme(protected val context: Context) {

    /** 样式表 */
    private val stylesheet: MutableMap<String, MapStylesheet> = mutableMapOf()

    /**
     * 获取样式表
     */
    fun getStylesheet(name: String): MapStylesheet {
        if (stylesheet.containsKey(name)) return stylesheet[name]!!
        return onCreateStylesheet(name).also { stylesheet[name] = it }
    }

    /**
     * 创建样式表
     */
    protected abstract fun onCreateStylesheet(name: String): MapStylesheet

    /**
     * 释放资源
     */
    open fun destroy() {
        stylesheet.forEach { it.value.destroy() }
        stylesheet.clear()
    }
}

