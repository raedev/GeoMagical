/**
 * 全局扩展函数
 * @author RAE
 * @date 2022/11/01
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.magical.map.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magical.map.exception.MapException
import java.lang.reflect.Type

/**
 * throw map exception
 */
@Throws(MapException::class)
internal inline fun mapError(message: String): Nothing = throw MapException(message)

/**
 * 克隆对象（基于JSON实现）
 */
fun <E> List<E>.cloneToList(clazz: Class<E>): List<E> {
    val gson = Gson()
    val json = gson.toJson(this)
    val type: Type = TypeToken.getParameterized(List::class.java, clazz).type
    return gson.fromJson(json, type)
}

