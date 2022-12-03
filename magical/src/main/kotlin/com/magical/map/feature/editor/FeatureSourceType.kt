package com.magical.map.feature.editor

/**
 * 来源类型
 * @author RAE
 * @date 2022/11/29
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
enum class FeatureSourceType {

    /** 原始数据 */
    SOURCE,

    /** 切割数据 */
    CUT,

    /** 合并数据 */
    UNION,

    /** 合并后的基准数据 */
    STANDARD
}
