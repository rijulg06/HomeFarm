package com.rijulg.homefarm.recyclerView

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.play.integrity.internal.ac
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.AppActivity
import com.rijulg.homefarm.ChatFragment
import com.rijulg.homefarm.R
import com.rijulg.homefarm.models.Post
import com.rijulg.homefarm.models.User
import org.w3c.dom.Text

class PostsAdapter (val context: Context, val posts: List<Post>) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(posts[position])

    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post) {

            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
            val chatButton: ImageButton = itemView.findViewById(R.id.chatButton)
            val ivPost: ImageView = itemView.findViewById(R.id.ivPost)
            val tvRelativeTime: TextView = itemView.findViewById(R.id.tvRelativeTime)
            val tvFruit: TextView = itemView.findViewById(R.id.tvFruit)

            tvName.text = post.user?.name
            tvDescription.text = post.description
            Glide.with(context).load(post.imageUrl).into(ivPost)
            tvRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creationTimeMs)
            tvFruit.text = post.fruit

            chatButton.setOnClickListener { v ->

                val toUser = posts[bindingAdapterPosition].user
                val bundle = Bundle()
                bundle.putSerializable("toUser", toUser)

                val activity = v!!.context as AppActivity
                val chatFragment = ChatFragment()
                chatFragment.arguments = bundle
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, chatFragment).commit()
            }
        }
    }
}