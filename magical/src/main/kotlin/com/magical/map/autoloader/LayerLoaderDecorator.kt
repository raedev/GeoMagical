package com.magical.map.autoloader

import com.magical.map.layer.LayerInfo

/**
 * 图层加载装饰器
 * @author RAE
 * @date 2022/11/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal abstract class LayerLoaderDecorator(
    protected val wrapper: LayerLoader
) : LayerLoader(wrapper.mapContext) {

    override fun loadLayers(): List<LayerInfo> {
        val layers = this.wrapper.loadLayers()
        val result = mutableListOf<LayerInfo>()
        layers.forEach { result.add(this.onAdapterLayerInfo(it)) }
        if (layers is MutableList<LayerInfo>) layers.clear()
        return result
    }

}