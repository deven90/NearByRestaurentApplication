package com.example.myrestaurentapplication.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Photo(
    @SerializedName("height")
    var height: Int,
    @SerializedName("html_attributions")
    var htmlAttributions: List<String>,
    @SerializedName("photo_reference")
    var photoReference: String,
    @SerializedName("width")
    var width: Int
) : Parcelable