package com.magical.map.feature.editor

/**
 * 图斑编辑器接口
 * @author RAE
 * @date 2022/11/29
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IFeatureEditor {

    /**
     * 编辑处理
     */
    fun <E : Any> onHandle(
        engine: FeatureEngine<E>,
        sources: List<FeatureEditEntity>,
        geometry: E?,
        selected: List<FeatureEditEntity>
    ): List<FeatureEditEntity>

    /**
     * 结果处理
     */
    fun handleResult(
        standardList: MutableList<FeatureEditEntity>,
        features: MutableList<FeatureEditEntity>
    ): MutableList<FeatureEditEntity>

}