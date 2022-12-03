package com.magical.map.themes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * 主题的GSON处理
 * @author RAE
 * @date 2022/11/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class ThemeGson {

    private class BitmapTypeAdapter : JsonSerializer<Bitmap>, JsonDeserializer<Bitmap> {
        override fun serialize(
            src: Bitmap?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive("base64")
        }

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Bitmap {
            return BitmapFactory.decodeFile("${Environment.getExternalStorageDirectory()}/test.jpg")
        }

    }

    internal val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Bitmap::class.java, BitmapTypeAdapter())
        .create()

    internal fun toJson(obj: Any) = gson.toJson(obj)

    internal fun <T> fromJsonToMap(json: String): Map<String, T>? {
        val type = object : TypeToken<HashMap<String, T>>() {}.type
        return gson.fromJson(json, type)
    }
}