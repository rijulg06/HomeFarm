package com.rijulg.homefarm.recyclerView

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.integrity.internal.t
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.R
import com.rijulg.homefarm.models.Message
import com.rijulg.homefarm.models.User
import org.w3c.dom.Text

class MessageAdapter (val context: Context, private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_from, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(message: Message) {

            val toUser: TextView = itemView.findViewById(R.id.toUser)
            val messageTo: CardView = itemView.findViewById(R.id.messageTo)
            val toMessageText: TextView = itemView.findViewById(R.id.toMessageText)
            val toMessageTime: TextView = itemView.findViewById(R.id.toMessageTime)

            val messageFrom: CardView = itemView.findViewById(R.id.messageFrom)
            val fromMessageText: TextView = itemView.findViewById(R.id.fromMessageText)
            val fromMessageTime: TextView = itemView.findViewById(R.id.fromMessageTime)

            if(message.fromUser?.uid == Firebase.auth.currentUser?.uid) {

                toUser.isGone = true
                messageTo.isGone = true
                toMessageText.isGone = true
                toMessageTime.isGone = true

                fromMessageText.text = message.messageText
                fromMessageTime.text = DateUtils.getRelativeTimeSpanString(message.sentAt)

                messageFrom.isVisible = true
                fromMessageText.isVisible = true
                fromMessageTime.isVisible = true

            } else {

                messageFrom.isGone = true
                fromMessageText.isGone = true
                fromMessageTime.isGone = true

                toMessageTime.text = DateUtils.getRelativeTimeSpanString(message.sentAt)
                toUser.text = message.fromUser?.name
                toMessageText.text = message.messageText

                toUser.isVisible = true
                messageTo.isVisible = true
                toMessageText.isVisible = true
                toMessageTime.isVisible = true

            }

        }

    }

}
