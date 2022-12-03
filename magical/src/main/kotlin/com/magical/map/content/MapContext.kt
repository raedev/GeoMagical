package com.magical.map.content

import android.content.Context
import com.magical.map.themes.MapTheme
import java.util.concurrent.ConcurrentHashMap

/**
 * 地图Context，功能与Android的Context类似。
 * @author RAE
 * @date 2022/11/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class MapContext(val context: Context) {

    companion object {
        private const val COMMON_NAME: String = "common"
    }

    /** 资源缓存 */
    private val mResCacheMap: ConcurrentHashMap<String, MapResources> = ConcurrentHashMap()

    open val rootName: String = "Magical"

    /** 应用名称，一般是固定实现的。 */
    abstract val appName: String

    /** 地图主题 */
    abstract val theme: MapTheme

    /** 用户标识 */
    abstract val user: String

    /** 当前项目名称，若切换当前项目，请务必手动设置，对应的地图资源也会随之改变。 */
    var projectName: String = COMMON_NAME
        private set

    /** 公共文件夹 */
    val commonDir: MapDirectory by lazy {
        MapDirectory(context, appName, COMMON_NAME, null, rootName)
    }

    /** 当前项目所在的资源 */
    val resources: MapResources
        get() = getResources(projectName, user)

    /**
     * 获取资源，若不存在则自动创建
     */
    protected open fun getResources(name: String, dirName: String? = null): MapResources {
        if (mResCacheMap.containsKey(name)) return mResCacheMap[name]!!
        val res = MapResources(context, appName, name, dirName, rootName)
        mResCacheMap[name] = res
        return res
    }

    /**
     * 设置当前项目
     */
    fun setProject(name: String) {
        this.projectName = name
        // 清空资源缓存，使之重新创建
        mResCacheMap.clear()
        getResources(projectName)
    }

    /**
     * 释放资源，请在适当的时机手动调用。
     */
    fun destroy() {
        mResCacheMap.clear()
        theme.destroy()
    }

}