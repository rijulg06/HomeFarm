package com.rijulg.homefarm.recyclerView

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.AppActivity
import com.rijulg.homefarm.ChatFragment
import com.rijulg.homefarm.R
import com.rijulg.homefarm.models.Room
import com.rijulg.homefarm.models.User

class RecentChatAdapter(options: FirestoreRecyclerOptions<Room>) :
    FirestoreRecyclerAdapter<Room, RecentChatAdapter.RoomAdapterViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_chat, parent, false)
        return RoomAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomAdapterViewHolder, position: Int, room: Room) {

        if (room.fromUid == Firebase.auth.currentUser?.uid) {
            FirebaseFirestore.getInstance().collection("users").document(room.toUid).get()
                .addOnSuccessListener { userSnapshot ->
                    val lastUser = userSnapshot.toObject(User::class.java)
                    holder.recentUser.text = lastUser?.name
                }
        } else if (room.toUid == Firebase.auth.currentUser?.uid) {
            FirebaseFirestore.getInstance().collection("users").document(room.fromUid).get()
                .addOnSuccessListener { userSnapshot ->
                    val lastUser = userSnapshot.toObject(User::class.java)
                    holder.recentUser.text = lastUser?.name
                }
        }

        if (room.lastMessage?.fromUid == Firebase.auth.currentUser?.uid) {
            val recentMessageYou = holder.itemView.context.getString(R.string.recentMessageYou, room.lastMessage?.messageText)
            holder.recentMessage.text = recentMessageYou
        } else {
            holder.recentMessage.text = room.lastMessage?.messageText
        }

        holder.recentTime.text = room.lastMessage?.sentAt.toString()

        holder.itemView.setOnClickListener { v ->

            if (room.fromUid == Firebase.auth.currentUser?.uid) {
                FirebaseFirestore.getInstance().collection("users").document(room.toUid).get()
                    .addOnSuccessListener { userSnapshot ->
                        val toUser = userSnapshot.toObject(User::class.java)

                        val bundle = Bundle()
                        bundle.putSerializable("toUser", toUser)

                        val activity = v!!.context as AppActivity
                        val chatFragment = ChatFragment()
                        chatFragment.arguments = bundle
                        activity.supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, chatFragment).commit()

                    }
            } else if (room.toUid == Firebase.auth.currentUser?.uid) {
                FirebaseFirestore.getInstance().collection("users").document(room.fromUid).get()
                    .addOnSuccessListener { userSnapshot ->
                        val toUser = userSnapshot.toObject(User::class.java)

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


    class RoomAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val recentUser: TextView = itemView.findViewById(R.id.recentUser)
        val recentMessage: TextView = itemView.findViewById(R.id.recentMessage)
        val recentTime: TextView = itemView.findViewById(R.id.recentTime)

    }
}