package com.magical.map.layer.feature

import com.magical.map.core.FeatureAttrIterator
import com.magical.map.themes.symbol.MapSymbol
import com.magical.map.themes.symbol.TextMapSymbol

/**
 * 单个矢量渲染接口
 * @author RAE
 * @date 2022/11/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FeatureItemStyleRender {

    /**
     * 渲染图斑
     * @param iterator 属性迭代器
     * @return
     */
    fun renderItem(iterator: FeatureAttrIterator): MapSymbol

    /**
     * 渲染图斑注记
     */
    fun renderText(
        iterator: FeatureAttrIterator,
        text: String
    ): TextMapSymbol

    /**
     * 渲染高亮图斑样式
     */
    fun renderHighlight(iterator: FeatureAttrIterator): MapSymbol
}