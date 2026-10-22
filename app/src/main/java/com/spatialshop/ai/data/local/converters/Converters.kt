package com.spatialshop.ai.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.spatialshop.ai.domain.model.ProductColor

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value ?: emptyList<String>())
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromColorList(value: List<ProductColor>?): String {
        return gson.toJson(value ?: emptyList<ProductColor>())
    }

    @TypeConverter
    fun toColorList(value: String?): List<ProductColor> {
        if (value.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<ProductColor>>() {}.type
        return gson.fromJson(value, listType)
    }
}
