package com.example.myrestaurentapplication.utility

import com.example.myrestaurentapplication.model.RestaurantDetails

class RatingsComparator : Comparator<RestaurantDetails> {
    override fun compare(p0: RestaurantDetails, p1: RestaurantDetails): Int {
        return p0.rating.compareTo(p1.rating)
    }
}