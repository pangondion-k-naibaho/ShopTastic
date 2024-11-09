package com.shoptastic.client.data.model.response.products

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RatingProductResponse(
    @field:SerializedName("rate")
    val rate: Double,

    @field: SerializedName("count")
    val count: Int
):Parcelable