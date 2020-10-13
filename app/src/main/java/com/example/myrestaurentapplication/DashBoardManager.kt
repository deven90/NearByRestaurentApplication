package com.example.myrestaurentapplication

import android.content.Context
import android.location.Location
import android.util.Log
import com.example.myrestaurentapplication.apis.APIInterface
import com.example.myrestaurentapplication.apis.ApiClient
import com.example.myrestaurentapplication.model.PlacesResponse
import com.example.myrestaurentapplication.model.RestaurantDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardManager() {

    private val TAG = DashBoardManager.javaClass.simpleName
    var currentLat = 0.0
    var currentLong = 0.0
    val restaurants = ArrayList<RestaurantDetails>()

    companion object {
        private var dashBoardManager: DashBoardManager? = null

        @JvmStatic
        fun getDashBoardManager(): DashBoardManager {
            if (dashBoardManager == null) {
                dashBoardManager = DashBoardManager()
            }
            return dashBoardManager!!
        }
    }

    fun fetchPlaceDetails(context: Context) {
        val dashBoardManagerListener: DashBoardManagerListener = context as DashBoardManagerListener
        val apiService = ApiClient.getClient().create(
            APIInterface::class.java
        )
        val call =
            apiService.getNearByRestaurants(
                "$currentLat,$currentLong",
                5000,
                "restaurant",
                context.getString(R.string.google_maps_key)
            )
        call.enqueue(object : Callback<PlacesResponse> {
            override fun onResponse(
                call: Call<PlacesResponse>,
                response: Response<PlacesResponse?>
            ) {
                val root = response.body()
                if (response.isSuccessful) {
                    when (root!!.status) {
                        "OK" -> {
                            val results = root.results
                            Log.i(TAG, "Fetch Restaurants")
                            var i = 0
                            restaurants.clear()
                            while (i < results.size) {
                                val info = results[i]
//                                val placeId: String = info.placeId
                                val photoUrl = info.photos?.let {
                                    val photoReference: String =
                                        it[0].photoReference
                                    ApiClient.base_url + "place/photo?maxwidth=100&photoreference=" + photoReference +
                                            "&key=" + context.getString(R.string.google_maps_key)
                                }

                                val result = FloatArray(1)
                                Location.distanceBetween(
                                    currentLat,
                                    currentLong,
                                    info.geometry.location.lat,
                                    info.geometry.location.lng,
                                    result
                                )

                                val tempRestaurantDetails = RestaurantDetails(
                                    info.name,
                                    photoUrl,
                                    info.rating,
                                    result[0],
                                    info.geometry.location.lat,
                                    info.geometry.location.lng,
                                    info.vicinity
                                )
                                restaurants.add(tempRestaurantDetails)
                                Log.i("Names  ", info.name)
                                i++
                            }
                            dashBoardManagerListener.onGetRestaurantsSuccess(restaurants)
                        }
                        "ZERO_RESULTS" -> {
                            dashBoardManagerListener.onGetRestaurantsFailed("No matches found near you")
                        }
                        "OVER_QUERY_LIMIT" -> {
                            dashBoardManagerListener.onGetRestaurantsFailed("You have reached the Daily Quota of Requests")
                        }
                        else -> {
                            dashBoardManagerListener.onGetRestaurantsFailed("Something went wrong!!!")
                        }
                    }
                } else if (response.code() != 200) {
                    dashBoardManagerListener.onGetRestaurantsFailed("Error " + response.code() + " found.")
                }
            }

            override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                dashBoardManagerListener.onGetRestaurantsFailed("Error in Fetching Details,Please Refresh")
                call.cancel()
            }
        })
    }

    interface DashBoardManagerListener {

        fun onGetRestaurantsSuccess(restaurants: ArrayList<RestaurantDetails>)

        fun onGetRestaurantsFailed(message: String)
    }
}