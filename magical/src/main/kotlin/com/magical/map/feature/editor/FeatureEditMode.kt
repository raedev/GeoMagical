package com.magical.map.feature.editor

/**
 * 要素编辑类型
 * @author RAE
 * @date 2022/11/22
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
enum class FeatureEditMode {

    /**
     * 切割
     */
    Cut,

    /**
     * 合并
     */
    Union,

    /**
     * 调整图形
     */
    Edit,

    /**
     * 打点切割
     */
    Point,

    /**
     * 未定义类型
     */
    Unknown
}