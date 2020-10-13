package com.example.myrestaurentapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.myrestaurentapplication.fragments.RestaurantsListFragment
import com.example.myrestaurentapplication.fragments.RestaurantsMapFragment
import com.example.myrestaurentapplication.model.RestaurantDetails
import com.example.myrestaurentapplication.utility.shortToast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.tabs.TabLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), DashBoardManager.DashBoardManagerListener {
    private lateinit var mSettingsClient: SettingsClient
    private val TAG = this.javaClass.simpleName
    private lateinit var dashBoardManager: DashBoardManager
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        dashBoardManager = DashBoardManager.getDashBoardManager()
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        requestForLocationPermission()
    }

    private val locationSettingRequest: LocationSettingsRequest
        get() {
            if (mLocationSettingsRequest == null) {
                val builder = LocationSettingsRequest.Builder()
                builder.addLocationRequest(locationRequest)
                mLocationSettingsRequest = builder.build()
            }
            return mLocationSettingsRequest!!
        }

    private val locationRequest: LocationRequest
        get() {
            if (mLocationRequest == null) {
                mLocationRequest = LocationRequest()
                mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
                mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
                mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            return mLocationRequest!!
        }

    class SectionsPagerAdapter(
        private val context: Context,
        fm: FragmentManager,
        private val restaurants: ArrayList<RestaurantDetails>
    ) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val TAB_TITLES = arrayOf(
            R.string.tab_listing,
            R.string.tab_map
        )

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                RestaurantsListFragment.newInstance(restaurants)
            } else {
                RestaurantsMapFragment.newInstance(restaurants)
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return context.resources.getString(TAB_TITLES[position])
        }

        override fun getCount(): Int {
            return 2
        }
    }

    override fun onGetRestaurantsSuccess(restaurants: ArrayList<RestaurantDetails>) {
        constraintLoader.visibility = GONE
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager, dashBoardManager.restaurants)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.visibility = VISIBLE
        tabs.setupWithViewPager(viewPager)
    }

    override fun onGetRestaurantsFailed(message: String) {
        constraintLoader.visibility = GONE
        shortToast(message)
    }

    private fun requestForLocationPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        startLocationUpdates()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", this@DashboardActivity.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                        return
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { }
            .onSameThread()
            .check()
    }


    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        mSettingsClient
            .checkLocationSettings(locationSettingRequest)
            .addOnSuccessListener(this) { locationSettingsResponse ->
                mFusedLocationProviderClient?.let {
                    it.lastLocation.addOnSuccessListener(this@DashboardActivity,
                        OnSuccessListener<Location?> { location ->
                            if (location != null) {
                                dashBoardManager.currentLat = location.latitude
                                dashBoardManager.currentLong = location.longitude
                                Log.e(
                                    TAG,
                                    "${dashBoardManager.currentLat},${dashBoardManager.currentLong}"
                                )
                                constraintLoader.visibility = VISIBLE
                                dashBoardManager.fetchPlaceDetails(this@DashboardActivity)
                            } else {
                                this@DashboardActivity.shortToast("Error in fetching the location")
                            }
                        })

                }
            }
            .addOnFailureListener(this) { e ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.e(
                            TAG,
                            "Location settings are not satisfied. Attempting to upgrade " + "location settings "
                        )
                        runOnUiThread {
                            try {
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.e(TAG, "PendingIntent unable to execute request.")
                            }
                        }
                    }
                    LocationSettingsStatusCodes.CANCELED -> {
                        val errorMessage =
                            "User Cancelled Setting Permission"
                        Log.e(TAG, errorMessage)
                        runOnUiThread {
                            this.shortToast(errorMessage)
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        runOnUiThread {
                            this.shortToast(errorMessage)
                        }
                    }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.e(TAG, "User agreed to make required location settings changes.")
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> {
                    Log.e(TAG, "User chose not to make required location settings changes.")
                    finish()
                }
            }
        }
    }


    // location updates interval - 10sec
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
    val REQUEST_CHECK_SETTINGS = 1110

}