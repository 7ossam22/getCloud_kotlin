//package com.example.cloud.adapter.homeAdapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.cloud.R
//import com.example.cloud.data.CloudData
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter
//import com.firebase.ui.firestore.FirestoreRecyclerOptions
//
//class HomeAdapter2(options : FirestoreRecyclerOptions<CloudData>) : FirestoreRecyclerAdapter<CloudData, HomeAdapter2.MyViewHolder>(
//    options
//) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter2.MyViewHolder {
//        return MyViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: HomeAdapter2.MyViewHolder, position: Int, model: CloudData) {
//         holder.apply {
//             Glide
//                 .with(img.context)
//                 .load(model.link)
//                 .centerCrop()
//                 .into(img)
//         }
//    }
//
//    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name = itemView.findViewById<EditText>(R.id.item_name)!!
//        val size = itemView.findViewById<EditText>(R.id.item_size)!!
//        val date = itemView.findViewById<EditText>(R.id.item_date)!!
//        val img = itemView.findViewById<ImageView>(R.id.item_image)!!
//    }
//}