package com.magical.map.layer

import com.magical.map.view.MapMotionEvent
import com.magical.map.view.MotionEventType

/**
 * 矢量文件图层
 * @author RAE
 * @date 2022/11/13
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class FeatureMapLayer : MapLayer() {

    override fun onTouchEvent(event: MapMotionEvent): Boolean {
        when (event.type) {
            MotionEventType.Click -> {
                if (event.geometry.isEmpty()) {
                    onOutsideClick()
                    return false
                }
                return onFeatureClick(event.geometry)
            }
            MotionEventType.LongClick -> {
                if (event.geometry.isEmpty()) return false
                return onFeatureLongClick(event.geometry)
            }
            else -> return false
        }
    }

    /**
     * 点击矢量图形
     */
    protected open fun onFeatureClick(geometryList: MutableList<Any>): Boolean = false

    /**
     * 长按矢量图形
     */
    protected open fun onFeatureLongClick(geometryList: MutableList<Any>): Boolean = false

    /**
     * 点击外面
     */
    protected open fun onOutsideClick() = Unit
}