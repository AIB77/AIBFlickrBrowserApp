package com.example.aibflickrbrowserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_row.view.*

class RVAdapter (val activity: MainActivity, private val photos: ArrayList<Image>): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_row,
            parent,
             false
        )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val photo = photos[position]

        holder.itemView.apply {
            tvImageText.text = photo.title
            Glide.with(activity).load(photo.link).into(ivThumbnail)
            llItemRow.setOnClickListener { activity.openImg(photo.link) }
        }
    }

    override fun getItemCount() = photos.size
}
