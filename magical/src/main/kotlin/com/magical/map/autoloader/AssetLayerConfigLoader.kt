package com.magical.map.autoloader

import android.content.res.AssetManager
import com.magical.map.layer.LayerInfo
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

/**
 * Asset配置文件装载器，图层配置目录：assets/layers
 * @author RAE
 * @date 2022/11/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class AssetLayerConfigLoader(wrapper: LayerLoader) : LayerLoaderDecorator(wrapper) {

    private val assetManager: AssetManager
        get() = mapContext.context.assets

    /** key = asset文件名 */
    private val layerMap = mutableMapOf<String, LayerInfo>()

    override fun loadLayers(): List<LayerInfo> {
        // 首先读取所有的图层信息
        layerMap.clear()
        listAssetsToLayerInfo()
        val result = mutableListOf<LayerInfo>()
        // 已上一个装饰者的图层为基础
        result.addAll(super.loadLayers())
        layerMap.forEach { m ->
            result.find { it == m.value } ?: result.add(m.value)
        }
        return result
    }

    override fun onAdapterLayerInfo(layerInfo: LayerInfo): LayerInfo {
        // 找到对应的配置图层
        val name = layerInfo.name
        layerMap[name]?.let {
            // 这里只取上一个装饰者的路径即可
            it.path = layerInfo.path
            return it
        }
        return layerInfo
    }

    /**
     * 遍历Assets的图层文件信息
     */
    private fun listAssetsToLayerInfo() {
        assetManager.list("layers")?.forEach {
            kotlin.runCatching {
                val filename = File(it).nameWithoutExtension
                val json = readString("layers/$it") ?: return@forEach
                val layer = json.toLayerInfo()
                layerMap[filename] = layer
            }
        }
    }

    /**
     * 读取Assets文本内容
     */
    private fun readString(path: String): String? {
        var stream: InputStream? = null
        try {
            stream = assetManager.open(path)
            val bytes = stream.readBytes()
            val out = ByteArrayOutputStream()
            out.write(bytes)
            val result = out.toString()
            out.close()
            stream.close()
            return result
        } catch (e: Throwable) {
            kotlin.runCatching { stream?.close() }.getOrNull()
        }
        return null
    }
}