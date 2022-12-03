package com.magical.map.view

import com.magical.map.model.MagicalPoint

/**
 * 地图触摸事件包装
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused")
class MapMotionEvent private constructor(
    /** 触摸事件类型 */
    var type: MotionEventType = MotionEventType.Click,
    /** 触摸时地理位置点 */
    var point: MagicalPoint? = null,
    /** 发生触摸事件时捕获到的矢量对象 */
    var geometry: MutableList<Any> = mutableListOf()
) {
    companion object {

        /** 使用较为频繁，采用静态对象缓存 */
        private val instance: MapMotionEvent = MapMotionEvent()

        /**
         * 获取实例
         */
        fun obtain(eventType: MotionEventType): MapMotionEvent {
            instance.type = eventType
            instance.geometry.clear()
            return instance
        }
    }

    override fun toString(): String {
        return "MapTouchEvent[$type]: Point=${point?.x},${point?.y} Geometry=${geometry.size}"
    }
}