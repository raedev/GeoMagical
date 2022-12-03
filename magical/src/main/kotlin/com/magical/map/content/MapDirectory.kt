package com.magical.map.content

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.util.Log
import com.magical.map.utils.MapLog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 地图目录统一规范管理
 * @author RAE
 * @date 2022/11/01
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class MapDirectory internal constructor(
    private val context: Context,
    /** 应用名称，程序的根目录：/root/app/ */
    private val appName: String,
    /** 项目名称，/root/app/project/ */
    private val projectName: String,
    /** 用户名，/root/app/project/user */
    private val userName: String? = null,
    /** 根目录：/root/ */
    private val rootName: String = "Magical",
) {

    init {
        initDir()
    }

    /** 根目录：/ 或者 context.filesDir */
    private val root: File
        get() {
            var sdcard = Environment.getExternalStorageDirectory()
            if (!sdcard.canWrite()) {
                sdcard = context.getExternalFilesDir(null) ?: context.filesDir
            }
            return makeDir(sdcard, rootName)
        }

    /** APP目录：[/app/] */
    private val appDir: File
        get() = makeDir(root, appName)

    /** 项目目录：[/app/project/]  */
    val projectDir: File
        get() = makeDir(appDir, projectName)

    /** 用户目录：[/app/project/user/] */
    private val userDir: File
        get() = if (userName == null) projectDir else makeDir(projectDir, userName)

    /** 日志目录：[/app/project/logs/] */
    val logDir: File
        get() = makeDir(makeDir(projectDir, "logs"), todayDate())

    /** 用户数据目录：[/app/project/user/database/] */
    val dataDir: File
        get() = makeDir(userDir, "database")

    /** 图层目录：[/app/project/user/layer/] */
    val layerDir: File
        get() = makeDir(userDir, "layer")

    /** 矢量目录：[/app/project/user/layer/feature] */
    val featureDir: File
        get() = makeDir(layerDir, "feature")

    /** 地图地图目录：[/app/project/user/layer/basemap] */
    val basemapDir: File
        get() = makeDir(layerDir, "basemap")

    /** 地图缓存目录：[/app/project/user/layer/cache] */
    val layerCacheDir: File
        get() = makeDir(layerDir, "cache")

    /** 临时目录：[/app/project/user/temp/] */
    val tempDir: File
        get() = makeDir(userDir, "temp")

    /** 照片目录：[/app/project/user/photo] */
    val photoDir: File
        get() = makeDir(userDir, "photo")

    /** 视频目录：[/app/project/user/video] */
    val videoDir: File
        get() = makeDir(userDir, "video")

    /** 音频目录：[/app/project/user/audio] */
    val audioDir: File
        get() = makeDir(userDir, "audio")

    /** 输出目录：[/app/project/user/output] */
    val outputDir: File
        get() = makeDir(userDir, "output")

    /** 配置目录：[/app/project/user/config] */
    val configDir: File
        get() = makeDir(userDir, "config")

    /** 今天输出目录：[/app/project/user/output/yyyy-MM-dd] */
    val todayOutputDir: File
        get() = makeDir(outputDir, todayDate())

    /**
     * 获取文件夹，若不存在自动创建。例如：getDir(outputDir, "demo")
     * @param parent 父目录
     * @param name 目录名称
     */
    fun makeDir(parent: File, name: String): File {
        val dir = File(parent, name)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /**
     * 初始化默认的目录，如图层目录、数据库目录、配置目录
     */
    internal fun initDir() {
        Log.d(MapLog.TAG, "初始化地图目录：$basemapDir")
    }

    @SuppressLint("SimpleDateFormat")
    private fun todayDate(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date())
    }
}