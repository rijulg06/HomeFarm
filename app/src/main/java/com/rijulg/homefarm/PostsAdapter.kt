package com.rijulg.homefarm

import android.content.Context
import android.media.Image
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rijulg.homefarm.models.Post

class PostsAdapter (val context: Context, val posts: List<Post>) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = posts[position]
        holder.tvName.text = currentItem.user?.name
        holder.tvDescription.text = currentItem.description
        Glide.with(context).load(currentItem.imageUrl).into(holder.ivPost)
        holder.tvRelativeTime.text = DateUtils.getRelativeTimeSpanString(currentItem.creationTimeMs)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvName : TextView = itemView.findViewById(R.id.tvName)
            val tvDescription : TextView = itemView.findViewById(R.id.tvDescription)
            val ivPost : ImageView = itemView.findViewById(R.id.ivPost)
            val tvRelativeTime : TextView = itemView.findViewById(R.id.tvRelativeTime)

    }
}