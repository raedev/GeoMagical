package com.magical.map.layer

/**
 * 地图图层信息
 * @author RAE
 * @date 2022/11/01
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused")
data class LayerInfo(
    // 图层名称
    var name: String,
    // 图层显示的名称
    var title: String,
    // 装载图层的完整类名，如：com.magical.map.layer.MapLayer
    var clazz: String,
    /** 图层类型，详见[LayerType] */
    var type: Int = 0,
    // 图层顺序
    var orderNo: Int = 0,
    // 在线图层地址
    var url: String? = null,
    // 本地路径
    var path: String? = null,
    // 是否可见
    var visible: Boolean = true,
    // 图层透明度
    var alpha: Int = 100,
    // 图层参数信息，用于特定图层的自定义处理
    var params: MutableMap<String, Any?> = mutableMapOf(),
) {
    /**
     * 创建图层实例
     */
    fun create(): MapLayer {
        return (Class.forName(this.clazz).newInstance() as MapLayer).also {
            it.layerInfo = this
        }
    }
}