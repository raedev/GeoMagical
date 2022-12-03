@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.magical.map.feature.editor

/**
 * 要素编辑结果
 * @author RAE
 * @date 2022/11/22
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FeatureEditResult constructor(
    /** 编辑模式 */
    var editMode: FeatureEditMode = FeatureEditMode.Unknown,
    /** 是否成功 */
    var isSuccess: Boolean = false,
    /** 错误消息 */
    var message: String? = null,
    /** 结果集 */
    var data: List<FeatureEditEntity> = emptyList()
) {

    fun successfully(): FeatureEditResult = apply {
        this.isSuccess = true
        this.message = null
    }

    fun error(message: String): FeatureEditResult = apply {
        this.isSuccess = false
        this.message = message
    }
}