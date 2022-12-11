package com.magical.map.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.magical.map.core.MapController
import com.magical.map.layer.MapLayer
import com.magical.map.layer.MapLayerList

/**
 * 地图容器，可以看成一个地图代理类。
 * 真正的地图实现使用[addView]的方法添加为子View，然后对抽象方法进行实现。
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class MagicalMapView : FrameLayout {

    /** 真正的地图 */
    private var mBaseMapView: View? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    /** 地图控制器 */
    var controller: MapController? = null

    fun setBaseMapView(view: View) {
        if (mBaseMapView != null) error("base mapview has bean added")
        mBaseMapView = view
        val lp = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.addView(view, 0, lp)
    }

    /**
     * 获取真正的地图
     */
    fun getBaseMapView(): View {
        return mBaseMapView ?: error("base mapview not added yet")
    }

    // region 抽象方法

    /**
     * 开启地图调试模式
     */
    abstract fun setMapDebug(enable: Boolean)

    /**
     * 设置当前地图中心点
     * @param x 经度
     * @param y 纬度
     * @param anim 是否启用动画
     */
    abstract fun setCenter(x: Double, y: Double, anim: Boolean = true)

    /**
     * 将地图缩放到一个区域
     * @param top 上
     * @param bottom 下
     * @param left 左
     * @param right 右
     * @param scale 相对地图大小缩放的比例
     * @param anim 是否启用动画
     */
    abstract fun setMapExtent(
        top: Double,
        bottom: Double,
        left: Double,
        right: Double,
        scale: Float = 1F,
        anim: Boolean = true
    )

    abstract fun setMaxLevel(level: Float)

    abstract fun setMinLevel(level: Float)

    abstract fun getMapLevel(): Float

    abstract fun getMapRotation(): Float

    /**
     * 设置地图层级[0,24]，国内地图普遍只支持到21级别。
     * @param anim 是否启用动画
     */
    abstract fun setMapLevel(@FloatRange(from = 0.0, to = 24.0) level: Float, anim: Boolean = true)

    /**
     * 旋转地图
     * @param rotation 旋转角度
     * @param anim 是否启用动画
     */
    abstract fun setMapRotation(rotation: Float, anim: Boolean = false)

    /**
     * 刷新地图
     * 1、需要对当前地图图层进行重新排序
     */
    abstract fun refresh()

    /**
     * 设置地图是否可以触发点击事件
     */
    abstract fun setMapClickable(enable: Boolean)

    /**
     * 设置地图是否可以触发双击事件
     */
    abstract fun setMapDoubleClickable(enable: Boolean)

    /**
     * 设置地图是否可以触发长按事件
     */
    abstract fun setMapLongClickable(enable: Boolean)

    /**
     * 设置地图是否可以触发旋转事件
     */
    abstract fun setMapRotatable(enable: Boolean)

    /**
     * 添加图层
     */
    abstract fun addLayer(layer: MapLayer): Boolean

    /**
     * 移除图层
     */
    abstract fun removeLayer(layer: MapLayer): Boolean

    /**
     * 图层重新排序
     */
    abstract fun resortLayers(layers: MapLayerList)

    /**
     * 裁剪地图
     */
    open fun clipMapBound(rect: Rect) = Unit

    // endregion


    /**
     * 销毁地图
     */
    open fun destroy() {
        this.controller = null
        this.removeAllViews()
    }

    // region 框架内部调用方法

    internal fun attachController(controller: MapController) {
        this.controller = controller
    }

    internal fun detachController() {
        this.controller = null
    }

    /**
     * 添加图层布局
     */
    internal fun setLayerView(view: MapLayerView) {
        val p = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.addView(view, p)
    }

    /**
     * 移除图层布局
     */
    internal fun removeLayerView(view: MapLayerView) {
        this.removeView(view)
    }

    /**
     * 生命周期
     */
    open fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }


    // endregion
}
