package com.magical.map.themes.style

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.magical.map.themes.symbol.MapSymbol

/**
 * 支持通过Android 的 styles.xml 方式创建地图样式
 * @author RAE
 * @date 2022/11/05
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class AndroidMapStyle(
    private val context: Context,
    name: String
) : MapStyle(name) {

    private lateinit var mSymbol: MapSymbol

    /** 样式定义 */
    protected abstract val styleableRes: IntArray

    /** 样式实现 */
    protected abstract val styleId: Int

    override var symbol: MapSymbol
        get() = mSymbol
        set(value) {
            mSymbol = value
        }

    /**
     * 解析Android样式
     */
    internal abstract fun parseStyle(helper: MapStyleParseHelper): MapSymbol

    /**
     * 加载样式
     */
    fun loadStyle(): MapStyle = apply {
        if (this::mSymbol.isInitialized) return@apply
        val attr = context.obtainStyledAttributes(styleId, styleableRes)
        val helper = MapStyleParseHelper(name, context, attr)
        this.mSymbol = parseStyle(helper)
        attr.recycle()
    }

    protected fun TypedArray.getBitmap(resId: Int): Bitmap? {
        val index = this.getIndex(resId)
        if (index < 0) return null
        val drawableId = this.getResourceId(index, -1)
        if (drawableId == -1) return null
        return BitmapFactory.decodeResource(context.resources, resId)
    }

}