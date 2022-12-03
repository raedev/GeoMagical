@file:Suppress("MemberVisibilityCanBePrivate")

package com.magical.map.layer.feature

import com.magical.map.core.FeatureAttrIterator
import com.magical.map.themes.style.MapStylesheet
import com.magical.map.themes.symbol.MapSymbol
import com.magical.map.themes.symbol.TextMapSymbol

/**
 * 默认的单个矢量渲染
 * @author RAE
 * @date 2022/11/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class DefaultFeatureItemStyleRender(var stylesheet: MapStylesheet) :
    FeatureItemStyleRender {

    /**
     * 状态字段
     */
    var stateField: String = "TAG"

    override fun renderItem(iterator: FeatureAttrIterator): MapSymbol {
        val name = if (iterator.contains(stateField)) {
            "$stateField.${iterator[stateField]}"
        } else MapStylesheet.STYLE_DEFAULT
        return stylesheet.getStyle(name).symbol
    }

    override fun renderText(iterator: FeatureAttrIterator, text: String): TextMapSymbol {
        val name = if (iterator.contains(stateField)) {
            "TEXT.$stateField.${iterator[stateField]}"
        } else MapStylesheet.STYLE_DEFAULT
        return stylesheet.getStyle(name).symbol as TextMapSymbol
    }

    override fun renderHighlight(iterator: FeatureAttrIterator): MapSymbol {
        return stylesheet.getStyle(MapStylesheet.STYLE_HIGHLIGHT).symbol
    }

}