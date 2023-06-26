package com.example.jello_projectmanag.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedMembers(
    var id: String = "",
    var image: String = ""
): Parcelable