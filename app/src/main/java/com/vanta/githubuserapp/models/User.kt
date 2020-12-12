package com.vanta.githubuserapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var username: String? = "-",
    var name: String? = "-",
    var location: String? = "-",
    var repository: Int? = 0,
    var company: String? = "-",
    var followers: Int? = 0,
    var following: Int? = 0,
    var avatar: String? = "-"
) : Parcelable