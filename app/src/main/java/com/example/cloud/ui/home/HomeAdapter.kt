package com.example.cloud.ui.home

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cloudData = data[position]
        holder.apply {
            Log.i("data", cloudData.name)
            name.text = cloudData.name
            size.text = cloudData.size
            date.text = cloudData.date
            onFilteringData(img, cloudData.type, cloudData.link)
        }
    }

    override fun getItemCount() = data.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.item_name)!!
        val size = itemView.findViewById<TextView>(R.id.item_size)!!
        val date = itemView.findViewById<TextView>(R.id.item_date)!!
        val img = itemView.findViewById<ImageView>(R.id.item_image)!!
    }

    //Displaying specific images for each data type after filtering it
    private fun onFilteringData(img: ImageView, type: String, link: String) {
        when {
            type.startsWith("image") -> {
                Glide.with(img.context)
                    .load(link)
                    .optionalCircleCrop()
                    .placeholder(R.drawable.ic_baseline_cloud_24)
                    .into(img)
            }
            type.endsWith("android.package-archive") -> {
                img.setImageResource(R.drawable.ic_baseline_android_24)
            }
            type.endsWith("pdf") -> {
                img.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24)
            }
            type.startsWith("audio") -> {
                img.setImageResource(R.drawable.ic_baseline_audiotrack_24)
            }
            else -> {
                img.setImageResource(R.drawable.ic_baseline_insert_drive_file_24)
            }
        }
    }
}