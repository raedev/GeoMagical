package com.magical.map.themes.style

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.magical.map.R
import com.magical.map.themes.symbol.*

/**
 * 样式解析帮助类
 * @author RAE
 * @date 2022/11/05
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class MapStyleParseHelper(
    private val name: String,
    private val context: Context,
    private val attr: TypedArray
) {
    private fun TypedArray.getBitmap(resId: Int): Bitmap? {
        val index = this.getIndex(resId)
        if (index < 0) return null
        val drawableId = this.getResourceId(index, -1)
        if (drawableId == -1) return null
        return BitmapFactory.decodeResource(context.resources, resId)
    }

    fun parsePoint(): PointMapSymbol {
        return PointMapSymbol(
            name,
            attr.getColor(R.styleable.GmPointStyle_gm_point_color, Color.RED),
            attr.getDimension(R.styleable.GmPointStyle_gm_point_size, 12F),
            attr.getInt(R.styleable.GmPointStyle_gm_point_alpha, 100),
            attr.getBitmap(R.styleable.GmPointStyle_gm_point_bmp)
        )
    }

    fun parseLine(): LineMapSymbol {
        return LineMapSymbol(
            name,
            attr.getColor(R.styleable.GmLineStyle_gm_line_color, Color.RED),
            attr.getDimension(R.styleable.GmLineStyle_gm_line_width, 12F),
            attr.getInt(R.styleable.GmLineStyle_gm_line_alpha, 100),
            attr.getBitmap(R.styleable.GmLineStyle_gm_line_bmp)
        )
    }

    fun parsePolygon(): PolygonMapSymbol {
        return PolygonMapSymbol(
            name,
            attr.getColor(R.styleable.GmFillStyle_gm_fill_color, Color.RED),
            LineMapSymbol(
                "${name}.Outline",
                attr.getColor(R.styleable.GmFillStyle_gm_outline_color, Color.RED),
                attr.getDimension(R.styleable.GmFillStyle_gm_outline_width, 12F),
                attr.getInt(R.styleable.GmFillStyle_gm_outline_alpha, 100),
                attr.getBitmap(R.styleable.GmFillStyle_gm_outline_alpha)
            ),
            attr.getInt(R.styleable.GmFillStyle_gm_fill_alpha, 100),
            attr.getBitmap(R.styleable.GmFillStyle_gm_fill_bmp)
        )
    }

    fun parseText(): MapSymbol {
        return TextMapSymbol(
            name,
            attr.getColor(R.styleable.GmTextStyle_gm_font_color, Color.BLACK),
            attr.getDimension(R.styleable.GmTextStyle_gm_font_size, 12F),
            attr.getInt(R.styleable.GmTextStyle_gm_font_alpha, 100),
            attr.getDimension(R.styleable.GmTextStyle_gm_font_stroke_width, 0F),
            attr.getColor(R.styleable.GmTextStyle_gm_font_stroke_color, Color.TRANSPARENT),
            attr.getDimension(R.styleable.GmTextStyle_gm_text_border_width, 0F),
            attr.getColor(R.styleable.GmTextStyle_gm_text_border_color, Color.TRANSPARENT),
            attr.getColor(R.styleable.GmTextStyle_gm_text_background_color, Color.TRANSPARENT)
        )
    }
}