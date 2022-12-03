@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package com.magical.map.feature.editor

import com.google.gson.GsonBuilder
import com.magical.map.utils.MapLog
import kotlin.math.max

/**
 * 要素编辑器
 * @author RAE
 * @date 2022/11/22
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class FeatureEditor<G : Any> protected constructor(val engine: FeatureEngine<G>) {

    /** 编辑类型 */
    var editMode: FeatureEditMode = FeatureEditMode.Unknown

    /** 操作数据源 */
    var datasource: MutableList<FeatureEditEntity> = mutableListOf()

    /** 预览的数据 */
    var previewResult: FeatureEditResult? = null

    /** 是否可以保存的 */
    val preservable: Boolean
        get() = previewResult?.isSuccess ?: false


    /**
     * 通过Geometry获取FeatureEditEntity
     */
    fun getFeature(geometry: G): FeatureEditEntity? {
        return datasource.firstOrNull { it.geometry == geometry }
    }

    fun clear() {
        previewResult = null
        datasource.clear()
    }

    /**
     * 执行编辑
     */
    fun performEdit(geometry: G?, selectedList: List<FeatureEditEntity>): FeatureEditResult {
        val result = FeatureEditResult(editMode)
        return try {
            val editor: IFeatureEditor = when (editMode) {
                FeatureEditMode.Cut -> FeatureCutter()
                FeatureEditMode.Union -> FeatureUnion()
                FeatureEditMode.Edit -> TODO()
                FeatureEditMode.Point -> TODO()
                FeatureEditMode.Unknown -> TODO()
            }
            // 执行编辑
            val data = editor.onHandle(engine, datasource, geometry, selectedList)
            if (data.isEmpty()) return result.error("错误的操作，请检查图形的正确性。")
            // 最后的结果列表
            var features = mutableListOf<FeatureEditEntity>()
            // 源数据
            features += datasource.clone(selectedList)
            // 重新排号
            data.forEach {
                it.no = makeOrderNo(features)
                it.newNo = it.no
                features.add(it)
            }
            // 交回给编辑器处理最后的结果
            val standardList = selectedList.clone()
            features = editor.handleResult(standardList, features)
            // 重新排序
            result.data = features.resort()
            // 打印输出
            MapLog.d("============编辑结果=============")
            result.data.forEach { MapLog.d(it.toString()) }
            // 保存预览的数据
            return result.successfully().also { previewResult = it }
        } catch (ex: Throwable) {
            result.error(ex.message ?: "操作失败，请重试")
        }
    }


    /**
     * 克隆基准数据
     */
    private fun List<FeatureEditEntity>.clone(removeItems: List<FeatureEditEntity> = emptyList()): MutableList<FeatureEditEntity> {
        val result = mutableListOf<FeatureEditEntity>()
        val gson = GsonBuilder().create()
        val type = FeatureEditEntity::class.java
        this.forEach {
            if (removeItems.contains(it)) return@forEach
            val json = gson.toJson(it)
            val m = gson.fromJson(json, type)
            m.geometry = it.geometry
            result.add(m)
        }
        return result
    }


    /**
     * 获取图斑号序号。
     * 解题思路：图斑号是按照顺序排列的，所以我们对原始图斑列表根据序号进行排序；
     * 那么假设排序的结果：1、2、4、5、6，则中间缺一个3；
     * 当遍历集合到第三个的时候发现他的序号并不是3，那么他就是缺的这个；
     * 如果都没有，那么放到最后一个去；
     *
     * @param features 图斑列表
     */
    protected fun makeOrderNo(features: List<FeatureEditEntity>): Int {
        // 先排序
        features.resort().forEachIndexed { i, feature ->
            val index = feature.no
            val m = i + 1
            // 找到缺口
            if (index > 0 && index != m) return m
        }
        // 最后一个位置
        return max(1, features.size + 1)
    }

    /**
     * 根据No序号重新排序
     */
    protected fun List<FeatureEditEntity>.resort(): List<FeatureEditEntity> {
        return this.sortedWith { a, b -> a.no - b.no }
    }

}