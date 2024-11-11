package com.shoptastic.client.data.model.other

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductSavedWithCount(
    val productSaved: ProductSaved,
    val count: Int
):Parcelable