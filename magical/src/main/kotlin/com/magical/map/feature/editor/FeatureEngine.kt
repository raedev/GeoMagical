package com.magical.map.feature.editor

/**
 * 要素引擎
 * @author RAE
 * @date 2022/11/23
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FeatureEngine<T> {

    /**
     * 图形合并
     * @param a 图形1
     * @param b 图形2
     * @return 合并后的图形，失败返回空
     */
    fun union(a: T, b: T): T?

    /**
     * 切割
     * @param cutter 操作图形
     * @param source 被切割图形
     * @return 切割集合，失败返回[emptyList]
     */
    fun cut(cutter: T, source: T): List<T>

    /**
     * 图形转JSON
     */
    fun toJson(geometry: T): String?

    /**
     * JSON 转图形
     */
    fun fromJson(json: String): T?
}