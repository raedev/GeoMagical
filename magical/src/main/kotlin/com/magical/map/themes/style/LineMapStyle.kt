package com.magical.map.themes.style

import android.content.Context
import com.magical.map.R
import com.magical.map.themes.symbol.MapSymbol

/**
 * 线样式
 * @author RAE
 * @date 2022/11/05
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class LineMapStyle(context: Context, name: String, override val styleId: Int) :
    AndroidMapStyle(context, name) {

    override val styleableRes: IntArray = R.styleable.GmLineStyle
    override fun parseStyle(helper: MapStyleParseHelper): MapSymbol {
        return helper.parseLine()
    }

    init {
        loadStyle()
    }
}