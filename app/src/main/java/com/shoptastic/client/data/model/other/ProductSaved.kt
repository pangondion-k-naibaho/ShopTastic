package com.shoptastic.client.data.model.other

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shoptastic.client.data.model.response.products.ProductResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class ProductSaved(
    @PrimaryKey val dataId: String,
    val productResponse: ProductResponse,
    val isChecklisted: Boolean = false,
):Parcelable