package com.rijulg.homefarm.models

data class Room(
    var fromUser: User? = null,
    var toUser: User? = null,
    var lastMessage: Message? = null,
    var read: Boolean = false
)