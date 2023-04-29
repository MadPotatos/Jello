package com.example.jello_projectmanag.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var image: String = "",
    var mobile: Long = 0,
    var fcmToken: String = ""

): Parcelable
