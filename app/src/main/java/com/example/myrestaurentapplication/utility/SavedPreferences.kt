package com.example.myrestaurentapplication.utility

import android.content.Context
import android.content.SharedPreferences

internal class SavedPreferences private constructor() {
    private val DISTANCE_UNIT = "distance_unit"
    private val SORTING = "sorting"
    private var sharedPreferences: SharedPreferences? = null
    fun setDistanceUnitData(value: Boolean?): Boolean {
        return sharedPreferences!!.edit().putBoolean(DISTANCE_UNIT, value!!).commit()
    }

    val distanceUnitData: Boolean
        get() = if (!sharedPreferences!!.contains(DISTANCE_UNIT)) {
            false
        } else {
            sharedPreferences!!.getBoolean(DISTANCE_UNIT, false)
        }

    companion object {
        private var savePreferences: SavedPreferences? = null
        private fun init(context: Context) {
            if (savePreferences == null) {
                savePreferences = SavedPreferences()
                savePreferences!!.sharedPreferences =
                    context.getSharedPreferences("AppData", Context.MODE_PRIVATE)
            }
        }

        fun getInstance(context: Context): SavedPreferences? {
            if (savePreferences == null) {
                init(context)
            }
            return savePreferences
        }
    }
}