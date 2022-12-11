package com.magical.map.autoloader

import com.magical.map.layer.LayerInfo
import java.io.File

/**
 * 用户配置文件装载器
 * 1、用户图层配置目录：../common/config/layers
 * 2、系统默认配置目录：assets/layers
 * @author RAE
 * @date 2022/11/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class LayerConfigLoader(
    wrapper: LayerLoader
) : LayerLoaderDecorator(wrapper) {

    private val configDir: File = File(mapContext.commonDir.configDir, "layers")
    private val layers = mutableMapOf<String, LayerInfo>()

    override fun loadLayers(): List<LayerInfo> {
        // 加载图层
        layers.clear()
        configDir.listFiles()?.forEach { file ->
            val layer = file.toLayerInfo() ?: return@forEach
            layers[layer.name] = layer
        }

        val result = mutableListOf<LayerInfo>()
        result.addAll(super.loadLayers())
        result.addAll(layers.values)
        return result
    }

    override fun onAdapterLayerInfo(layerInfo: LayerInfo): LayerInfo {
        val config = layers[layerInfo.name] ?: return layerInfo

        // 这里只适配一些用户可配置的属性，因为在上一个装饰者已经初始化好图层的基础信息。
        layerInfo.title = config.title
        layerInfo.alpha = config.alpha
        layerInfo.orderNo = config.orderNo
        layerInfo.visible = config.visible
        layerInfo.params = config.params

        // 适配好了之后移除，方便后面批量添加
        layers.remove(layerInfo.name)

        return layerInfo
    }

}