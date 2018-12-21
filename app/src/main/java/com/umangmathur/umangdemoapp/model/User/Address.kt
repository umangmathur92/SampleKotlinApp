package com.umangmathur.umangdemoapp.model.User

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    @SerializedName("city") val city: String,
    @SerializedName("geo") val geoCoordinates: Geo,
    @SerializedName("street") val street: String,
    @SerializedName("suite") val suite: String,
    @SerializedName("zipcode") val zipCode: String
) : Parcelable