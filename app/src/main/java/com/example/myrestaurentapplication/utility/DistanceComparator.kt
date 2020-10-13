package com.example.myrestaurentapplication.utility

import com.example.myrestaurentapplication.model.RestaurantDetails

class DistanceComparator : Comparator<RestaurantDetails> {
    override fun compare(p0: RestaurantDetails, p1: RestaurantDetails): Int {
        return p0.distance.compareTo(p1.distance)
    }
}