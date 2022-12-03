package com.magical.map.model

/**
 * 要素实体
 * @author RAE
 * @date 2022/11/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IFeature {

    fun setPrimaryValue(value: String)

    fun getPrimaryValue(): String

    fun setGeometryJson(json: String)

    fun getGeometryJson(): String
}