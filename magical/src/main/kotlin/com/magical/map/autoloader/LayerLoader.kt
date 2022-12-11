package com.magical.map.autoloader

import com.google.gson.Gson
import com.magical.map.content.MapContext
import com.magical.map.layer.LayerInfo
import java.io.File

/**
 * 图层加载器（装饰者模式）
 * @author RAE
 * @date 2022/11/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class LayerLoader(internal val mapContext: MapContext) {

    private val gson by lazy { Gson() }

    /**
     * 加载图层信息
     */
    abstract fun loadLayers(): List<LayerInfo>

    /**
     * 当遍历被装饰者图层时回调的方法
     * @param layerInfo 图层信息
     */
    abstract fun onAdapterLayerInfo(layerInfo: LayerInfo): LayerInfo


    protected fun LayerInfo.toJson(): String {
        return gson.toJson(this)
    }

    protected fun fromJson(json: String): LayerInfo {
        return gson.fromJson(json, LayerInfo::class.java)
    }

    protected fun String.toLayerInfo(): LayerInfo = fromJson(this)

    protected fun File.toLayerInfo(): LayerInfo? {
        if (!exists()) return null
        val json = this.readText()
        return json.toLayerInfo()
    }
}