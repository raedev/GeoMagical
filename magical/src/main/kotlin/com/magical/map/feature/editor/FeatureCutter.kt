package com.magical.map.feature.editor

/**
 * 图斑切割机
 * @author RAE
 * @date 2022/11/29
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("UNCHECKED_CAST")
class FeatureCutter : IFeatureEditor {

    override fun <E : Any> onHandle(
        engine: FeatureEngine<E>,
        sources: List<FeatureEditEntity>,
        geometry: E?,
        selected: List<FeatureEditEntity>
    ): List<FeatureEditEntity> {
        // 切割结果
        geometry ?: error("请先绘制切割图形")
        if (selected.size != 1) error("只能对一个图形进行切割，请检查")
        val entity = selected[0]
        val geometries = engine.cut(geometry, entity.geometry as E)
        val result = mutableListOf<FeatureEditEntity>()
        geometries.forEachIndexed { index, it ->
            val item = FeatureEditEntity(
                entity.id, entity.title, it, 0, index, FeatureEditType.ADD, FeatureSourceType.CUT
            )
            result.add(item)
        }
        return result
    }

    override fun handleResult(
        standardList: MutableList<FeatureEditEntity>,
        features: MutableList<FeatureEditEntity>
    ): MutableList<FeatureEditEntity> {
        // 基准数据（这里肯定是一个）
        val entity = standardList.single()
        features.forEach {
            if (it.newNo == entity.no) {
                // 更新基准数据
                it.editType = FeatureEditType.UPDATE
                it.sourceType = FeatureSourceType.STANDARD
                return@forEach
            }
        }
        return features
    }
}