package com.vanta.githubuserapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val username: String,
    val name: String,
    val location: String,
    val repository: Int,
    val company: String,
    val followers: Int,
    val following: Int,
    val avatar: Int
) : Parcelable