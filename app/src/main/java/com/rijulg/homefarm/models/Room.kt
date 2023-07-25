package com.rijulg.homefarm.models

data class Room(
    var fromUid: String = "",
    var toUid: String = "",
    var lastMessage: Message? = null
)