package com.magical.map.layer.feature

import com.magical.map.core.FeatureAttrIterator

/**
 * 矢量标签格式化，也叫图斑注记
 * @author RAE
 * @date 2022/11/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FeatureLabelFormatter {

    /**
     * 将矢量图层数据格式化为矢量标签
     * @param meta 矢量数据迭代器
     * @return 标签
     */
    fun format(meta: FeatureAttrIterator): String?
}