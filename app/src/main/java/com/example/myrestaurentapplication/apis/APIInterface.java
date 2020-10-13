package com.example.myrestaurentapplication.apis;

import com.example.myrestaurentapplication.model.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("place/nearbysearch/json?")
    Call<PlacesResponse> getNearByRestaurants(@Query(value = "location", encoded = true) String location, @Query(value = "radius", encoded = true) long radius, @Query(value = "type", encoded = true) String type, @Query(value = "key", encoded = true) String key);

}
