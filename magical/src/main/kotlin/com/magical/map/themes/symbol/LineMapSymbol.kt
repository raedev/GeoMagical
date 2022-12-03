package com.magical.map.themes.symbol

import android.graphics.Bitmap

/**
 * 线符号
 * @author RAE
 * @date 2022/11/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class LineMapSymbol(
    name: String,
    /** 线的颜色 */
    var color: Int,
    /** 线的宽度 */
    var width: Float,
    /** 透明度 */
    var alpha: Int = 100,
    /** 线填充图片，例如轨迹线 */
    var bitmap: Bitmap? = null
) : MapSymbol(name)