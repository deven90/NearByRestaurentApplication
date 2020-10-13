package com.example.myrestaurentapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myrestaurentapplication.DashBoardManager
import com.example.myrestaurentapplication.R
import com.example.myrestaurentapplication.model.RestaurantDetails
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RestaurantsMapFragment : Fragment() {
    private var restaurants = ArrayList<RestaurantDetails>()
    private lateinit var dashBoardManager: DashBoardManager
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurants =
            arguments?.getParcelableArrayList<RestaurantDetails>(ARG_RESTAURENTS) as ArrayList<RestaurantDetails>
        dashBoardManager = DashBoardManager.getDashBoardManager()
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val currentLatLong = LatLng(dashBoardManager.currentLat, dashBoardManager.currentLong)
        googleMap.addMarker(MarkerOptions().position(currentLatLong).title("You are here!"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 14.0f))
        this.map = googleMap

        for (i in 0 until restaurants.size) {
            val lati: Double = restaurants[i].latitude.toDouble()
            val longLat: Double = restaurants[i].longitude.toDouble()
            googleMap.addMarker(
                MarkerOptions().position(
                    LatLng(lati, longLat)
                )
                    .title(restaurants[i].name)
                    .snippet(restaurants[i].address)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_restaurants_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    companion object {

        private val ARG_RESTAURENTS: String = "restaurants"

        @JvmStatic
        fun newInstance(restaurants: ArrayList<RestaurantDetails>?): RestaurantsMapFragment {
            return RestaurantsMapFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_RESTAURENTS, restaurants)
                }
            }
        }
    }

}