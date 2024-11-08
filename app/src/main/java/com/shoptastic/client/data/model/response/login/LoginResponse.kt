package com.shoptastic.client.data.model.response.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse (
    @field:SerializedName("token")
    val token: String?,
): Parcelable