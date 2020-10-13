package com.example.myrestaurentapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrestaurentapplication.R
import com.example.myrestaurentapplication.model.RestaurantDetails

class RestaurantsAdapter(
    private val values: List<RestaurantDetails>
) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        Glide.with(holder.ivRestaurantPic.context)
            .load(item.photoUrl)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(holder.ivRestaurantPic)

        holder.tvRestaurantName.text = item.name
        holder.tvRestaurantDistance.text = item.distance.toString()
        holder.tvRestaurantRating.text = item.rating.toString()
        holder.tvRestaurantAddress.text = item.address
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivRestaurantPic: ImageView = view.findViewById(R.id.ivRestaurantPic)
        val tvRestaurantName: TextView = view.findViewById(R.id.tvRestaurantName)
        val tvRestaurantRating: TextView = view.findViewById(R.id.tvRestaurantRating)
        val tvRestaurantDistance: TextView = view.findViewById(R.id.tvRestaurantDistance)
        val tvRestaurantAddress: TextView = view.findViewById(R.id.tvRestaurantAddress)

        override fun toString(): String {
            return super.toString() + " '" + tvRestaurantName.text + "'"
        }
    }
}