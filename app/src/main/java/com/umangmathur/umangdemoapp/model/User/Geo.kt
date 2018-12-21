package com.umangmathur.umangdemoapp.model.User

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Geo(
    @SerializedName("lat") val latitude: String,
    @SerializedName("lng") val longitude: String
) : Parcelable