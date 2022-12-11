package com.magical.map.themes.style

import android.content.Context
import com.magical.map.R
import com.magical.map.themes.symbol.MapSymbol
import com.magical.map.themes.symbol.PolygonMapSymbol

/**
 * 面样式
 * @author RAE
 * @date 2022/11/05
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PolygonMapStyle(context: Context, name: String, override val styleId: Int) :
    AndroidMapStyle(context, name) {

    constructor(context: Context, symbol: PolygonMapSymbol) : this(context, symbol.name, 0) {
        super.symbol = symbol
    }

    override val styleableRes: IntArray = R.styleable.GmFillStyle
    override fun parseStyle(helper: MapStyleParseHelper): MapSymbol {
        return helper.parsePolygon()
    }

    init {
        loadStyle()
    }
}