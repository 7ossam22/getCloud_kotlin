package com.example.cloud.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloud.R
import com.example.cloud.api.FirebaseApi
import com.example.cloud.data.CloudData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeAdapter :ListAdapter<CloudData,HomeAdapter.MyViewHolder>(CloudDataDiffCallback()) {
    private val api = FirebaseApi.singleton()
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cloudData = getItem(position)
        holder.apply {
            Log.i("data", cloudData.name)
            name.text = cloudData.name
            size.text = cloudData.size
            date.text = cloudData.date
            size.text = onSizeCalculation(cloudData.size)
            onFilteringData(img, cloudData.type, cloudData.link)
            menu.setOnClickListener {
                val popupMenu = PopupMenu(menu.context, menu)
                popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.downloadItem -> Toast.makeText(
                            menu.context,
                            "Download Clicked",
                            Toast.LENGTH_LONG
                        ).show().equals(true)
                        R.id.deleteItem -> onDeleteItem(cloudData).equals(true)
                        else
                        -> false
                    }
                }
                popupMenu.show()
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.item_name)!!
        val size = itemView.findViewById<TextView>(R.id.item_size)!!
        val date = itemView.findViewById<TextView>(R.id.item_date)!!
        val img = itemView.findViewById<ImageView>(R.id.item_image)!!
        val menu = itemView.findViewById<ImageView>(R.id.item_menu)!!
    }

    //Displaying specific images for each data type after filtering it
    private fun onFilteringData(img: ImageView, type: String, link: String) {
        //Image filtering
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

    private fun onSizeCalculation(size: String): String {
        //Size filtering
        val sizeKB = (size.toDouble() / 1024)
        val sizeMB = sizeKB / 1024
        val sizeGB = sizeMB / 1024
        val sizeTB = sizeGB / 1024
        return when {
            sizeTB >= 1 -> sizeTB.toInt().toString() + "TB"
            sizeGB >= 1 -> sizeGB.toInt().toString() + "GB"
            sizeMB >= 1 -> sizeMB.toInt().toString() + "MB"
            sizeKB >= 1 -> sizeKB.toInt().toString() + "KB"
            else -> size + "Bytes"
        }


    }

    private fun onDeleteItem(item: CloudData) {
        uiScope.launch {
            api.deleteItem(item)
        }
    }



    class CloudDataDiffCallback : DiffUtil.ItemCallback<CloudData>(){
        override fun areItemsTheSame(oldItem: CloudData, newItem: CloudData): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CloudData, newItem: CloudData): Boolean {
            return oldItem == newItem
        }

    }
}