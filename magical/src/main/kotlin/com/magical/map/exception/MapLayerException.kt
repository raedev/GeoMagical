package com.magical.map.exception

/**
 * 地图图层异常
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MapLayerException : RuntimeException {

    constructor(message: String) : super(message)

    internal constructor(message: String, throwable: Throwable) : super(message, throwable)
}