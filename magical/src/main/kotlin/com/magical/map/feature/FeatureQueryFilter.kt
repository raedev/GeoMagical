package com.magical.map.feature

import com.magical.map.utils.MapLog


/**
 * 矢量查询过滤器
 * @author RAE
 * @date 2022/06/30
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate")
open class FeatureQueryFilter(where: String? = null) {

    protected val builder = StringBuilder()

    /**
     * 设置Where语句，之前设置的所有查询将失效。用于复杂的查询语句
     */
    var where: String? = null
        set(value) {
            builder.clear()
            builder.append(value)
        }

    private val and = " AND "
    private val or = " OR "
    private val like = " LIKE "

    init {
        where?.let { this.where = it }
    }

    /**
     * 数据库查询真正的值
     */
    protected fun Any.realValue() = when (this) {
        is String -> "'$this'" // 字符串带双引号
        else -> this.toString()
    }

    private fun operate(op: String, key: String, value: Any): FeatureQueryFilter {
        builder.append(op)
            .append("\"").append(key).append("\"")
            .append("=")
            .append(value.realValue())
        return this
    }

    /**
     * 字段相等
     */
    fun and(key: String, value: Any): FeatureQueryFilter = operate(and, key, value)

    /** 或 */
    fun or(key: String, value: Any): FeatureQueryFilter = operate(or, key, value)

    /** 模糊查询 */
    fun like(key: String, value: Any): FeatureQueryFilter = operate(like, key, value)

    /**
     * 分页
     */
    fun paging(page: Int, pageSize: Int): FeatureQueryFilter {
        builder.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(page * pageSize)
        return this
    }

    /**
     * 移除操作符
     */
    private fun String.removeOperate(op: String): String {
        return this.takeIf { it.startsWith(op) }?.replace(op, "") ?: this
    }

    fun toSql(): String {
        val sql = builder.toString().removeOperate(and).removeOperate(or).removeOperate(like)
        MapLog.d("图斑查询语句: $sql")
        return sql
    }

    override fun toString(): String = toSql()
}