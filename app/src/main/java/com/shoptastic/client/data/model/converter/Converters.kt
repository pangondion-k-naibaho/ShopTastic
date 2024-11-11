package com.shoptastic.client.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoptastic.client.data.model.response.products.ProductResponse


class Converters {

    private val gson = Gson()

    // Mengonversi ProductResponse ke String (JSON)
    @TypeConverter
    fun fromProductResponse(productResponse: ProductResponse): String {
        return gson.toJson(productResponse)
    }

    // Mengonversi String (JSON) kembali ke ProductResponse
    @TypeConverter
    fun toProductResponse(data: String): ProductResponse {
        val type = object : TypeToken<ProductResponse>() {}.type
        return gson.fromJson(data, type)
    }
}