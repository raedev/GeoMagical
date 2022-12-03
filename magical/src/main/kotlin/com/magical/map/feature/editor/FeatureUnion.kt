package com.magical.map.feature.editor

/**
 * 图斑合并
 * @author RAE
 * @date 2022/11/29
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("UNCHECKED_CAST")
class FeatureUnion : IFeatureEditor {

    override fun <E : Any> onHandle(
        engine: FeatureEngine<E>,
        sources: List<FeatureEditEntity>,
        geometry: E?,
        selected: List<FeatureEditEntity>
    ): List<FeatureEditEntity> {
        var temp: E? = null
        selected.forEachIndexed { index, it ->
            val item = it.geometry as E
            if (index == 0) {
                temp = item
                return@forEachIndexed
            }
            temp ?: return@forEachIndexed
            // 合并
            temp = engine.union(temp!!, item) ?: return@forEachIndexed
        }
        temp ?: error("图形合并失败，请检查图形是否正确")
        return arrayListOf(selected[0].copy().apply {
            this.geometry = temp!!
            this.editType = FeatureEditType.UPDATE
            this.sourceType = FeatureSourceType.STANDARD
        })
    }

    override fun handleResult(
        standardList: MutableList<FeatureEditEntity>,
        features: MutableList<FeatureEditEntity>
    ): MutableList<FeatureEditEntity> {
        // 基准数据
        val standard = features.find { it.sourceType == FeatureSourceType.STANDARD }
        standard ?: error("合并失败，无法找到合并后的图形。")
        standardList.forEach {
            if (it.no == standard.no) return@forEach
            it.sourceType = FeatureSourceType.UNION
            it.editType = FeatureEditType.DELETE
            features.add(it)
        }
        return features
    }
}