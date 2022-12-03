package com.magical.map.feature

import com.magical.map.model.IFeature

/**
 * 要素数据集
 * @author RAE
 * @date 2022/11/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class FeatureDataset {

    /**
     * 要素实体类
     */
    abstract fun <T : IFeature> getFeatureClass(): Class<T>

}