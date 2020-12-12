package com.vanta.githubuserapp.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val COMPANY = "company"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val AVATAR_URL = "avatar_url"
        }
    }

}