package com.rijulg.homefarm.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    var messageText: String = "",
    val fromUser: User? = null,
    var sentAt: Long = 0,
    var seen: Boolean = false
)