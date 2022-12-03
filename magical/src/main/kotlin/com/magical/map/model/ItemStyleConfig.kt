package com.magical.map.model

/**
 * 图斑样式配置
 * @author RAE
 * @date 2022/11/18
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class ItemStyleConfig(
    /** 填充颜色 */
    var fillColor: String = "#FFFFFF",
    /** 边框颜色 */
    var outlineColor: String = "#FFFFFF",
    /** 边框大小 */
    var outlineWidth: Float = 2F,

    /** 线颜色 */
    var lineColor: String = "#FFFFFF",
    /** 线大小 */
    var lineWidth: Float = 2F,
)