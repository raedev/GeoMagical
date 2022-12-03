package com.magical.map.model

/**
 * 定位模式
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
enum class LocationMode {

    /**
     * 正常模式
     */
    NORMAL,

    /**
     * 跟随位置模式
     */
    LOCATION,

    /**
     * 跟随方向传感器模式
     */
    COMPASS,

    /**
     * 未定义
     */
    UNKNOWN
}