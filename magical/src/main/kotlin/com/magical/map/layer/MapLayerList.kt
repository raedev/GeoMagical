package com.magical.map.layer

/**
 * 地图图层列表
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class MapLayerList : Iterable<MapLayer> {

    /** 图层集合，这是一个非常重要的对象 */
    private val mLayers: MutableList<MapLayer> = mutableListOf()

    /** 迭代器 */
    override fun iterator(): Iterator<MapLayer> = mLayers.iterator()

    /** 通过图层名称获取图层 */
    operator fun get(name: String): MapLayer? {
        return mLayers.singleOrNull { it.name.equals(name, true) }
    }

    // region 对图层的操作

    /**
     * 获取图层所在位置
     * @param layer 图层
     */
    fun indexOf(layer: MapLayer): Int = mLayers.indexOf(layer)

    /**
     * 图层是否已添加
     * @param layer 图层
     */
    fun contains(layer: MapLayer): Boolean = mLayers.contains(layer)

    /**
     * 添加图层，添加后列表重新排序。
     */
    fun addLayer(layer: MapLayer): Boolean {
        if (contains(layer.name)) return false
        return mLayers.add(layer).also { sort() }
    }

    /**
     * 移除图层，移除后列表重新排序。
     * @param layer 图层
     */
    fun removeLayer(layer: MapLayer): Boolean {
        return mLayers.remove(layer).also { sort() }
    }

    // endregion

    // region 对图层名称的操作

    /**
     * 获取图层所在位置
     * @param name 图层名称
     */
    fun indexOf(name: String): Int {
        return this[name]?.let { mLayers.indexOf(it) } ?: -1
    }

    /**
     * 图层是否已添加
     * @param name 图层名称
     */
    fun contains(name: String): Boolean = this[name] != null

    /**
     * 移除图层
     * @param name 图层名称
     */
    fun removeLayer(name: String): Boolean {
        return this[name]?.let { removeLayer(it) } ?: false
    }


    // endregion


    /**
     * 清除图层
     */
    fun clear() = mLayers.clear()

    /**
     * 对当前图层进行排序，排序规则：
     * 1、按照图层类型分类排序
     * 2、按orderNo字段排序
     */
    private fun sort() {
        val basemap = mLayers.filter { it.layerInfo.type == LayerType.Basemap.type }.sortByOrderNo()
        val other = mLayers.filter { it.layerInfo.type != LayerType.Basemap.type }.sortByOrderNo()
        mLayers.clear()
        // 底图位于最下面
        mLayers.addAll(basemap)
        // 其次是业务图层
        mLayers.addAll(other)
    }

    /**
     * 根据OrderNo字段排序
     */
    private fun List<MapLayer>.sortByOrderNo(): List<MapLayer> {
        return this.sortedWith { a, b ->
            a.layerInfo.type - b.layerInfo.type
        }
    }

}