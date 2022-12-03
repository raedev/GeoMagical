package com.magical.map.feature.editor

/**
 * 要素处理结果
 * @author RAE
 * @date 2022/11/22
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
enum class FeatureEditType {

    /**  新增数据，如切割的子图斑 */
    ADD,

    /** 更新的数据，如更新编号 */
    UPDATE,

    /**  删除数据，如合并图斑  */
    DELETE,

    /**  未定义  */
    UNDEFINED
}