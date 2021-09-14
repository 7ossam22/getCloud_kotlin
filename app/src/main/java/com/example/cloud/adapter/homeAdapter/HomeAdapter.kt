package com.example.cloud.adapter.homeAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloud.R
import com.example.cloud.data.CloudData

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    var data = listOf<CloudData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        val cloudData = data[position]
        holder.apply {
            Log.i("data",cloudData.name)
            name.text = cloudData.name
            size.text = cloudData.size
            date.text = cloudData.date
            type.text = cloudData.type
            Glide.with(img.context)
                .load(cloudData.link)
                .circleCrop()
                .placeholder(R.drawable.ic_person)
                .into(img)
        }
    }

    override fun getItemCount() = data.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.item_name)!!
        val size = itemView.findViewById<TextView>(R.id.item_size)!!
        val date = itemView.findViewById<TextView>(R.id.item_date)!!
        val img = itemView.findViewById<ImageView>(R.id.item_image)!!
        val type = itemView.findViewById<TextView>(R.id.item_annotation)!!
    }

}