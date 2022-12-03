package com.magical.map.themes.symbol

import android.graphics.Bitmap

/**
 * 面符号
 * @author RAE
 * @date 2022/11/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class PolygonMapSymbol(
    name: String,
    /** 面填充颜色 */
    var fillColor: Int,
    /** 外边框线 */
    var outline: LineMapSymbol,
    /** 面透明度 */
    var alpha: Int = 100,
    /** 线填充图片，例如轨迹线 */
    var bitmap: Bitmap? = null
) : MapSymbol(name)