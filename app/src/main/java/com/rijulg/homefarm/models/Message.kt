package com.rijulg.homefarm.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    var messageText: String = "",
    val fromUid: String = "",
    @ServerTimestamp val sentAt: Date? = null
)