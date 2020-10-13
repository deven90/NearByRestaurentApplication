package com.example.myrestaurentapplication.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class RestaurantDetails(
    var name: String? = null,
    var photoUrl: String? = null,
    var rating: Float = 0f,
    var distance: Float = 0f,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var address: String? = null,
    var phone_no: String? = null
) : Parcelable