package com.rijulg.homefarm.recyclerView

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rijulg.homefarm.R
import com.rijulg.homefarm.models.Post
import org.w3c.dom.Text

class PostsAdapter (val context: Context, val posts: List<Post>) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(posts[position])

//        val currentItem = posts[position]
//        holder.tvName.text = currentItem.user?.name
//        holder.tvDescription.text = currentItem.description
//        Glide.with(context).load(currentItem.imageUrl).into(holder.ivPost)
//        holder.tvRelativeTime.text = DateUtils.getRelativeTimeSpanString(currentItem.creationTimeMs)
//        holder.tvFruit.text = currentItem.fruit
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post) {

            val tvName : TextView = itemView.findViewById(R.id.tvName)
            val tvDescription : TextView = itemView.findViewById(R.id.tvDescription)
            val ivPost : ImageView = itemView.findViewById(R.id.ivPost)
            val tvRelativeTime : TextView = itemView.findViewById(R.id.tvRelativeTime)
            val tvFruit : TextView = itemView.findViewById(R.id.tvFruit)

            tvName.text = post.user?.name
            tvDescription.text = post.description
            Glide.with(context).load(post.imageUrl).into(ivPost)
            tvRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creationTimeMs)
            tvFruit.text = post.fruit

        }

    }
}