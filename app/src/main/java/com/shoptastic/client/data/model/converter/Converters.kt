package com.shoptastic.client.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.data.model.response.products.RatingProductResponse


class Converters {

    private val gson = Gson()

    // Mengonversi ProductResponse ke String (JSON)
    @TypeConverter
    fun fromRatingProductResponse(ratingProductResponse: RatingProductResponse): String {
        return gson.toJson(ratingProductResponse)
    }

    // Mengonversi String (JSON) kembali ke ProductResponse
    @TypeConverter
    fun toRatingProductResponse(data: String): RatingProductResponse {
        val type = object : TypeToken<RatingProductResponse>() {}.type
        return gson.fromJson(data, type)
    }
}