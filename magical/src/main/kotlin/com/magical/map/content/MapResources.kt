package com.magical.map.content

import android.content.Context

/**
 * 地图资源管理器，与Android的Resource具有相同的意义。
 * @author RAE
 * @date 2022/11/01
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
open class MapResources internal constructor(
    context: Context,
    /** 应用名称，程序的根目录：/app/ */
    appName: String,
    /** 项目名称，/app/project/ */
    projectName: String,
    /** 用户名，/app/project/user */
    userName: String? = null,
    /** 根目录：/root/ */
    private val rootName: String = "Magical",
) {

    /** 目录资源 */
    val directory: MapDirectory =
        MapDirectory(context, appName, projectName, userName, rootName)


}