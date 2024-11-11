package com.shoptastic.client.data.model.other

import android.os.Parcelable
import com.shoptastic.client.data.model.response.products.ProductResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductSavedWithCount(
    val savedProduct: ProductResponse,
    val count: Int,
    val isChecklisted: Boolean = false,
):Parcelable