package com.example.cloud.adapter.homeAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloud.R
import com.example.cloud.model.CloudData

class HomeAdapter : androidx.recyclerview.widget.ListAdapter<CloudData, HomeAdapter.MyViewHolder>(
    HomeAdapterDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cloudData = getItem(position)
        holder.bind(cloudData)
    }

    class MyViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.item_name)!!
        private val size = itemView.findViewById<TextView>(R.id.item_size)!!
        private val date = itemView.findViewById<TextView>(R.id.item_date)!!
        private val img = itemView.findViewById<ImageView>(R.id.item_image)!!
        fun bind(cloudData: CloudData) {
            apply {
                name.text = cloudData.name
                size.text = cloudData.size
                date.text = cloudData.date
                Glide.with(img.context)
                    .load(cloudData.link)
                    .circleCrop()
                    .placeholder(R.drawable.ic_person)
                    .into(img)
            }
        }

        companion object {
            fun from(parent: ViewGroup) = MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
            )
        }
    }


}

class HomeAdapterDiffUtil : DiffUtil.ItemCallback<CloudData>() {
    override fun areItemsTheSame(oldItem: CloudData, newItem: CloudData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CloudData, newItem: CloudData): Boolean {
        return oldItem == newItem
    }

}