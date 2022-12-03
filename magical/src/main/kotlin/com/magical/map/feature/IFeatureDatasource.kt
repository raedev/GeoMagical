package com.magical.map.feature

import com.magical.map.model.IFeature

/**
 * 要素数据源接口，执行对要素的操作
 * @author RAE
 * @date 2022/11/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IFeatureDatasource<T : IFeature> {

    /**
     * 要素唯一主键
     */
    fun primaryKey(): String

    /**
     * 添加要素
     */
    fun add(feature: T): Boolean

    /**
     * 删除要素
     */
    fun delete(feature: T): Boolean

    /** 根据主键删除 */
    fun delete(primaryValue: String): Boolean {
        return find(primaryValue)?.let { delete(it) } ?: false
    }

    /**
     * 更新要素
     */
    fun update(feature: T): Boolean {
        return update(feature.getPrimaryValue(), feature)
    }

    /**
     * 更新主键要素
     */
    fun update(primaryValue: String, feature: T): Boolean

    /**
     * 查询数量
     */
    fun count(filter: FeatureQueryFilter? = null): Int

    /**
     * 根据ID查询单个要素
     */
    fun find(id: Long): T?

    /** 根据主键查询 */
    fun find(primaryValue: String): T? {
        return querySingleOrNull(FeatureQueryFilter().and(primaryKey(), primaryValue))
    }

    /**
     * 查询单个要素
     */
    fun querySingleOrNull(filter: FeatureQueryFilter): T? {
        return query(filter).firstOrNull()
    }

    /**
     * 查询要素
     */
    fun query(filter: FeatureQueryFilter? = null): List<T>

}