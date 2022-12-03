package com.magical.map.layer

import android.location.Location
import android.view.MotionEvent
import com.github.raedev.compass.CompassManager
import com.github.raedev.compass.entity.CompassInfo
import com.github.raedev.compass.listener.CompassChangedListener
import com.magical.location.client.LocationClient
import com.magical.location.client.LocationListener
import com.magical.map.content.MapContext
import com.magical.map.model.LocationMode
import com.magical.map.utils.MapLog

/**
 * 位置图层
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class LocationMapLayer : MapLayer() {

    companion object {
        const val LAYER_NAME = "GPS"
    }

    /** 位置客户端 */
    private lateinit var client: LocationClient

    /** 方位角传感器 */
    private lateinit var compassManager: CompassManager

    /** 当前位置信息 */
    protected var currentLocation: Location? = null

    /** 当前方位角信息 */
    private var currentCompassInfo: CompassInfo? = null

    /** 上一次的方位角 */
    private var lastAzimuth: Int = 0

    /** 方位角回调周期，单位毫秒 */
    private val compassDelay: Int = 500

    /** 位置模式 */
    var locationMode: LocationMode = LocationMode.LOCATION
        set(value) {
            val old = field
            field = value
            if (value != old) {
                onLocationModeChanged(old, value)
            }
        }

    /** 方位角监听 */
    private val compassChangedListener = object : CompassChangedListener {
        override fun onCompassChanged(compass: CompassInfo) {
            this@LocationMapLayer.currentCompassInfo = compass
            if (lastAzimuth != compass.azimuth) {
                onRequestRefreshLayer(null, compass)
                lastAzimuth = compass.azimuth
            }
        }

        override fun onCompassException(e: Exception) {
            MapLog.e("方位角异常：${e.message}", "LocationMapLayer", e)
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            this@LocationMapLayer.currentLocation = location
            onRequestRefreshLayer(location, null)
        }
    }

    override fun onPrepare(context: MapContext, layerInfo: LayerInfo): PrepareLayerResult {
        client = LocationClient(context.context)
        if (!client.hasPermission()) {
            return PrepareLayerResult.error("没有位置权限无法开启位置图层")
        }
        return super.onPrepare(context, layerInfo)
    }

    override fun onAttach() {
        super.onAttach()
        // 位置客户端
        client.listener = locationListener
        client.start()
        // 方位角
        compassManager = CompassManager(context)
        // 不使用方位角的位置
        compassManager.setEnableLocation(false)
        // 传感器信息回调延迟
        compassManager.delay = compassDelay
        // 传感器注册监听
        compassManager.addCompassChangedListener(compassChangedListener)
        compassManager.start()
    }


    override fun onResume() {
        super.onResume()
        client.start()
        compassManager.start()
    }

    override fun onPause() {
        super.onPause()
        client.pause()
        compassManager.pause()
    }

    override fun onDetach() {
        super.onDetach()
        currentLocation = null
        currentCompassInfo = null
        client.stop()
        compassManager.destroy()
    }

    /**
     * 下一个位置模式
     */
    protected fun nextLocationMode(): LocationMode = when (locationMode) {
        LocationMode.NORMAL -> LocationMode.LOCATION
        LocationMode.LOCATION -> LocationMode.COMPASS
        LocationMode.COMPASS, LocationMode.UNKNOWN -> LocationMode.NORMAL
    }

    /**
     * 开始请求定位
     */
    fun location() {
        // 当前级别
        val mapview = magicalMapView ?: return
        val nextMode = this.nextLocationMode()
        if (nextMode != LocationMode.COMPASS) {
            // 恢复地图旋转
            mapview.setMapRotation(0f, true)
            compassManager.delay = compassDelay
        } else {
            compassManager.delay = 0
        }
        if (mapview.getMapLevel() < 17f) {
            this.locationMode = LocationMode.LOCATION
            mapview.setMapLevel(17f)
        } else {
            this.locationMode = nextMode
        }
        this.refresh()
    }

    protected open fun onLocationModeChanged(old: LocationMode, mode: LocationMode) = Unit

    /**
     * 请求刷新地图
     */
    abstract fun onRequestRefreshLayer(location: Location?, compass: CompassInfo?)

    override fun onTouchEvent(event: MotionEvent) {
        super.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_DOWN) {
            this.locationMode = LocationMode.NORMAL
        }
    }

    override fun refresh() {
        super.refresh()
        this.currentLocation?.let {
            onRequestRefreshLayer(it, this.currentCompassInfo)
        }
    }
}