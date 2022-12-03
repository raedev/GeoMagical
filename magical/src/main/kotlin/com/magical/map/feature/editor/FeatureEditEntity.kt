package com.magical.map.feature.editor

/**
 * 要素编辑数据源实体
 * @author RAE
 * @date 2022/11/22
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FeatureEditEntity(
    /** 原始ID */
    var id: String,
    /** 显示标题 */
    var title: String,
    /** 图形 */
    var geometry: Any,
    /** 图斑编号 */
    var no: Int = 0,
    /** 处理后的图斑编号 */
    var newNo: Int = 0,
    /** 编辑类型 */
    var editType: FeatureEditType = FeatureEditType.UNDEFINED,
    /** 来源类型 */
    var sourceType: FeatureSourceType = FeatureSourceType.SOURCE
) {
    override fun toString(): String {
        return "$title, id=$id, no=$no, newNo=$newNo, editType=$editType, sourceType=$sourceType"
    }

    /**
     * 复制对象
     */
    fun copy(): FeatureEditEntity {
        return FeatureEditEntity(id, title, geometry, no, newNo, editType, sourceType)
    }
}