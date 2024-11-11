package com.shoptastic.client.data.model.response.products

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class ProductResponse(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("price")
    val price: Double,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("rating")
    val rating: RatingProductResponse
):Parcelable