package com.example.myrestaurentapplication.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Geometry(
    @SerializedName("location")
    var location: Location
) : Parcelable