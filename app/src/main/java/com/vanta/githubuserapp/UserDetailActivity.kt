package com.vanta.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class UserDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val avatar: ImageView = findViewById(R.id.avatar)
        val tvName: TextView = findViewById(R.id.name)
        val tvUsername: TextView = findViewById(R.id.username)
        val tvLocation: TextView = findViewById(R.id.location)
        val tvRepository: TextView = findViewById(R.id.repository)
        val tvCompany: TextView = findViewById(R.id.company)
        val tvFollowers: TextView = findViewById(R.id.followers)
        val tvFollowing: TextView = findViewById(R.id.following)

        val user = intent.getParcelableExtra<User>("USER_INFO")

        Glide.with(avatar.context)
            .load(user?.avatar)
            .override(450, 450)
            .centerCrop()
            .into(avatar)

        tvName.text = user?.name
        tvUsername.text = user?.username
        tvLocation.text = user?.location
        tvRepository.text = user?.repository.toString()
        tvCompany.text = user?.company
        tvFollowers.text = user?.followers.toString()
        tvFollowing.text = user?.following.toString()

    }
}