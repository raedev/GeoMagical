package com.magical.map.utils

import android.annotation.SuppressLint
import android.util.Log
import com.magical.map.BuildConfig
import com.magical.map.core.MapController
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

/**
 * 地图日志
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object MapLog {

    @SuppressLint("SimpleDateFormat")
    private val FORMATTER = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    internal const val TAG: String = "GeoMagical"

    @Suppress("MemberVisibilityCanBePrivate")
    var DEBUG: Boolean = BuildConfig.DEBUG

    /**
     * 创建新的TAG
     */
    private fun String.newTag(): String {
        return if (this.isEmpty()) TAG else "$TAG.$this"
    }

    fun d(message: String, tag: String = "") {
        if (DEBUG) {
            Log.d(tag.newTag(), message)
            writeToFile(Log.DEBUG, tag, message)
        }
    }

    fun i(message: String, tag: String = "") {
        if (DEBUG) {
            Log.i(tag.newTag(), message)
            writeToFile(Log.INFO, tag, message)
        }
    }

    fun w(message: String, tag: String = "") {
        Log.w(tag.newTag(), message)
        writeToFile(Log.WARN, tag, message)
    }

    fun e(message: String, tag: String = "", throwable: Throwable? = null): Int {
        throwable ?: return Log.e(tag.newTag(), message)
        writeToFile(Log.ERROR, tag, message)
        return Log.e(tag.newTag(), message, throwable)
    }

    /**
     * 写日志文件
     */
    private fun writeToFile(level: Int, tag: String, content: String) = thread {
        kotlin.runCatching {
            val controller = MapController.get() ?: return@thread
            val logDir = controller.mapContext.commonDir.logDir
            if (!logDir.exists() || !logDir.canWrite()) return@thread
            var name = "map.debug.log"
            var levelName = "DEBUG"
            when (level) {
                Log.INFO -> {
                    levelName = "INFO"
                }
                Log.WARN -> {
                    levelName = "WARN"
                    name = "map.error.log"
                }
                Log.ERROR -> {
                    levelName = "ERROR"
                    name = "map.error.log"
                }
            }
            val logFile = File(logDir, name)
            if (!logFile.exists()) logFile.createNewFile()
            val text = "${FORMATTER.format(Date())} [$levelName] $tag: $content\r\n"
            logFile.appendText(text)
        }.getOrNull()
    }

}