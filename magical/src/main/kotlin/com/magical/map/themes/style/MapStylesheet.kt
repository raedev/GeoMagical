package com.magical.map.themes.style

import android.content.Context
import com.magical.map.utils.MapLog

/**
 * 地图样式表，管理样式的创建、获取、保存。
 * @author RAE
 * @date 2022/11/05
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class MapStylesheet(protected val context: Context, val name: String) {

    companion object {
        /**
         * 高亮的样式
         */
        const val STYLE_HIGHLIGHT: String = "highlight"
        const val STYLE_DEFAULT: String = "default"
    }

    /** 样式库 */
    private val mStyles = mutableMapOf<String, MapStyle>()

    /**
     * 创建样式
     */
    protected abstract fun onCreateStyle(name: String): MapStyle?

    /**
     * 保存样式
     */
    protected open fun onSaveStyle(mapStyle: MapStyle): Boolean {
        // TODO： 保存到本地JSON
        return true
    }

    /**
     * 保存样式
     */
    fun save(mapStyle: MapStyle): Boolean {
        val result = onSaveStyle(mapStyle)
        if (result) {
            // 更新缓存样式
            mStyles.remove(mapStyle.name)
            mStyles[mapStyle.name] = mapStyle
        }
        return result
    }

    /**
     * 获取地图样式
     */
    fun getStyle(name: String): MapStyle {
        if (mStyles.containsKey(name)) return mStyles[name]!!
        val style = onCreateStyle(name) ?: error("${this.name}样式表中找不到样式$name")
        mStyles[name] = style
        return style
    }


    internal fun destroy() = this.onDestroy()

    /**
     * 释放资源
     */
    protected open fun onDestroy() = Unit

    /**
     * 从JSON配置文件读取样式
     */
    fun loadFromJson(json: String) {
        val map = this.parseFromJson(json)
        // 保存用户配置的样式
        if (map.isNotEmpty()) {
            mStyles.putAll(map)
            MapLog.d("$name 样式表加载用户配置样式：$map")
        }
    }

    /**
     * 从JSON中解析样式
     */
    open fun parseFromJson(json: String): Map<String, MapStyle> {
        return emptyMap()
    }
}