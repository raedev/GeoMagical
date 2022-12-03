package com.magical.map.core

/**
 * 要素属性迭代器，一般属性是一个Map类型，该接口是为了对不同第三方库的依赖解耦。
 * @author RAE
 * @date 2022/11/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FeatureAttrIterator {
    fun contains(key: String): Boolean
    fun getValue(key: String): Any?

    operator fun get(key: String): Any? {
        return getValue(key)
    }
}