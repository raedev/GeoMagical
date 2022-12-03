package com.magical.map.autoloader

import com.magical.map.content.MapContext
import com.magical.map.content.MapDirectory
import com.magical.map.layer.LayerInfo
import com.magical.map.layer.LayerType
import java.io.File

/**
 * 图层文件加载器
 * @author RAE
 * @date 2022/11/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class LayerFileLoader(
    context: MapContext,
    /** 离线要素文件后缀 */
    private val featureExtension: String,
    /** 离线影像文件后缀 */
    private val basemapExtension: String,
) : LayerLoader(context) {

    private val common: MapDirectory
        get() = mapContext.commonDir

    private val configDir: File
        get() = File(common.configDir, "layers")

    override fun onAdapterLayerInfo(layerInfo: LayerInfo): LayerInfo = layerInfo

    // 装载公共目录的图层文件
    override fun loadLayers(): List<LayerInfo> {
        val layers = mutableListOf<LayerInfo>()
        layers.addAll(listFileToLayerInfo(common.basemapDir, LayerType.Basemap))
        layers.addAll(listFileToLayerInfo(common.featureDir, LayerType.Feature))
        return layers
    }

    /**
     * 列出文件并转换为图层信息
     */
    private fun listFileToLayerInfo(dir: File, type: LayerType): List<LayerInfo> {
        val files = dir.listFiles() ?: return emptyList()
        val result = mutableListOf<LayerInfo>()
        val extension = if (type == LayerType.Basemap) basemapExtension else featureExtension
        files.forEach { file ->
            if (file.extension == extension) result.add(fileToLayerInfo(file, type))
        }
        return result
    }

    /**
     * 文件转图层信息，这一步只需要确定好文件名和类型，其他的图层信息会交给下一个装饰器进行更新
     */
    private fun fileToLayerInfo(file: File, type: LayerType): LayerInfo {
        val name = file.nameWithoutExtension
        // 用户配置文件中是否已经配置
        val configFile = File(configDir, "$name.json")
        configFile.toLayerInfo()?.let { layer ->
            layer.path = file.path
            return layer
        }
        return LayerInfo(name, name, "", type.type, path = file.path)
    }
}