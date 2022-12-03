package com.magical.map.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * 地图图层视图，一个MapLayer可以由View控制，通过图层管理器添加到地图中去。
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class MapLayerView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
}