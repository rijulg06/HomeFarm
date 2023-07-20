package com.rijulg.homefarm.models

import java.io.Serializable

data class User(
    var uid: String = "",
    var name: String = ""
) : Serializable