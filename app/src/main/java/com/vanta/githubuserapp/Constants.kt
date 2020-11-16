package com.vanta.githubuserapp

class Constants {

    companion object {
        internal const val GITHUB_TOKEN  = "428d9ecd57e8a780fdcf406be10a0508ee973a59"
        internal const val GITHUB_SEARCH_URL = "https://api.github.com/search/users?q={username}"
        internal const val GITHUB_DETAIL_USER_URL = "https://api.github.com/users/{username}"
        internal const val GITHUB_LIST_FOLLOWER_URL = "https://api.github.com/users/{username}/followers"
        internal const val GITHUB_LIST_FOLLOWING_URL = "https://api.github.com/users/{username}/following"
    }

}