package com.umangmathur.umangdemoapp.model.User

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
    @SerializedName("bs") val bs: String,//Unsure of of what the abbreviation 'bs'is...
    @SerializedName("catchPhrase") val catchPhrase: String,
    @SerializedName("name") val name: String
) : Parcelable