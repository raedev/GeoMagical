package com.magical.map.layer.feature

import com.magical.map.core.FeatureAttrIterator

/**
 * 默认的标签格式化支持[标签]元素解析。
 * 配置示例：[state]
 * 配置字段映射：
 * "state.undo" = "待核查"
 * "state.save" = "已核查"
 * @author RAE
 * @date 2022/11/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class DefaultFeatureLabelFormatter(
    /** 标签格式，支持多个标签解析 */
    private var labelFormat: String,
    /** 字段映射 */
    val map: Map<String, String> = emptyMap()
) : FeatureLabelFormatter {

    private val formatGroups: Sequence<MatchResult>

    init {
        val regex = "\\[\\w+]".toRegex()
        formatGroups = regex.findAll(labelFormat)
    }

    override fun format(meta: FeatureAttrIterator): String? {
        if (labelFormat.isBlank()) return null
        var label: String = labelFormat
        for (group in formatGroups) {
            val field = group.value.removePrefix("[").removeSuffix("]")
            if (!meta.contains(field)) continue
            var value = meta[field]?.toString()
            // 映射关系
            val key = "$field.$value"
            if (map.containsKey(key)) {
                value = map[key]
            }
            value ?: continue
            label = label.replace(group.value, value)
        }
        return label
    }
}