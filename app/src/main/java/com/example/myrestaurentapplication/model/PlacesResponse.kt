package com.example.myrestaurentapplication.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PlacesResponse(
    @SerializedName("results")
    var results: List<Result>,
    @SerializedName("status")
    var status: String
) : Parcelable