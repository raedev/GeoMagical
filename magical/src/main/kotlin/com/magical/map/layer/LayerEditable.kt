@file:Suppress("unused")

package com.magical.map.layer

/**
 * 可编辑的图层
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface LayerEditable {

    /**
     * 开始执行
     */
    fun start() = Unit

    /**
     * 暂停执行，一般用在释放触摸操作
     */
    fun pause() = Unit

    /**
     * 撤销，即上一步
     */
    fun undo()

    /**
     * 重做，即下一步
     */
    fun redo()

    /**
     * 清除
     */
    fun clean()

    /**
     * 退出
     */
    fun exit()
}