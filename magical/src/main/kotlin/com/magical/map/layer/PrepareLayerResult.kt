package com.magical.map.layer

/**
 * 图层初始化结果
 * @author RAE
 * @date 2022/11/13
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PrepareLayerResult private constructor(
    val successfully: Boolean = true,
    val message: String? = null
) {
    companion object {
        fun success(): PrepareLayerResult {
            return PrepareLayerResult(true, null)
        }

        fun error(message: String): PrepareLayerResult {
            return PrepareLayerResult(false, message)
        }
    }

    override fun toString(): String {
        return message ?: "success"
    }
}