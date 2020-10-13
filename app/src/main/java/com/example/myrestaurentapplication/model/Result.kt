package com.example.myrestaurentapplication.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Result(
    @SerializedName("geometry")
    var geometry: Geometry,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("photos")
    var photos: List<Photo>,
    @SerializedName("place_id")
    var placeId: String,
    @SerializedName("price_level")
    var priceLevel: Int,
    @SerializedName("rating")
    var rating: Float,
    @SerializedName("vicinity")
    var vicinity: String
) : Parcelable