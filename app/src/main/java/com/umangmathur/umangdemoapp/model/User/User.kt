package com.umangmathur.umangdemoapp.model.User

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("address") val address: Address,
    @SerializedName("company") val company: Company,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("username") val username: String,
    @SerializedName("website") val website: String
) : Parcelable