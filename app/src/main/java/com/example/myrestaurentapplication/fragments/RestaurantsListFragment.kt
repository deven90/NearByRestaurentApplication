package com.example.myrestaurentapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrestaurentapplication.R
import com.example.myrestaurentapplication.adapters.RestaurantsAdapter
import com.example.myrestaurentapplication.model.RestaurantDetails
import kotlinx.android.synthetic.main.fragment_restaurants_list.view.*

class RestaurantsListFragment : Fragment() {

    private lateinit var rootView: View
    private var restaurants = ArrayList<RestaurantDetails>()
    private lateinit var restaurantsAdapter: RestaurantsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurants = arguments?.getParcelableArrayList<RestaurantDetails>(ARG_RESTAURENTS) as ArrayList<RestaurantDetails>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_restaurants_list, container, false)

        restaurantsAdapter = RestaurantsAdapter(restaurants)
        with(rootView.recyclerRestaurants) {
            layoutManager = LinearLayoutManager(this.context)
            adapter = restaurantsAdapter
        }

        return rootView
    }

    companion object {

        private val ARG_RESTAURENTS: String = "restaurants"

        @JvmStatic
        fun newInstance(restaurants: ArrayList<RestaurantDetails>?): RestaurantsListFragment {
            return RestaurantsListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_RESTAURENTS, restaurants)
                }
            }
        }
    }
}