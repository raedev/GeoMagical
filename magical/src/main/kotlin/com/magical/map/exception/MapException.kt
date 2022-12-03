package com.magical.map.exception

/**
 * 地图运行时异常
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class MapException : RuntimeException {

    internal constructor(message: String) : super(message)

    internal constructor(message: String, throwable: Throwable) : super(message, throwable)
}