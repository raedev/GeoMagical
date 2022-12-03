package com.magical.map.themes.symbol

import android.graphics.Color

/**
 * 文本符号
 * @author RAE
 * @date 2022/11/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class TextMapSymbol(
    name: String,
    /**
     * 颜色
     */
    var fontColor: Int = Color.RED,
    /**
     * 大小
     */
    var fontSize: Float = 12F,
    /**
     * 透明度
     */
    var fontAlpha: Int = 100,
    /**
     * 字体边描边宽度
     */
    var strokeWidth: Float = 0F,
    /**
     * 字体边描边颜色
     */
    var strokeColor: Int = Color.TRANSPARENT,
    /**
     * 字体边框宽度
     */
    var borderWidth: Float = 0F,
    /**
     * 字体边框颜色
     */
    var borderColor: Int = Color.TRANSPARENT,

    /**
     * 字体边背景颜色
     */
    var backgroundColor: Int = Color.TRANSPARENT
) : MapSymbol(name)