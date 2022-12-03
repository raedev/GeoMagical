package com.magical.map.themes.symbol

import android.graphics.Bitmap

/**
 * 点符号
 * @author RAE
 * @date 2022/11/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class PointMapSymbol(
    name: String,
    /** 点的颜色 */
    var color: Int,
    /** 点的大小 */
    var size: Float,
    /** 透明度 */
    var alpha: Int = 100,
    /** 点图片 */
    var bitmap: Bitmap? = null
) : MapSymbol(name)