package com.vanta.githubuserapp

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvUser: RecyclerView
    private lateinit var name: Array<String>
    private lateinit var username: Array<String>
    private lateinit var location: Array<String>
    private lateinit var repository: IntArray
    private lateinit var company: Array<String>
    private lateinit var followers: IntArray
    private lateinit var following: IntArray
    private lateinit var avatar: TypedArray
    private val users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareData()

        rvUser = findViewById(R.id.rv_user)
        rvUser.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rvUser.layoutManager = layoutManager

        val divider = DividerItemDecoration(rvUser.context, layoutManager.orientation)
        rvUser.addItemDecoration(divider)

        val listUserAdapter = ListUserAdapter(users)
        rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                showUserDetail(user)
            }
        })
    }

    private fun showUserDetail(user: User) {
        val userDetailIntent = Intent(this, UserDetailActivity::class.java)
        userDetailIntent.putExtra("USER_INFO", user)
        startActivity(userDetailIntent)
    }

    private fun prepareData() {
        getData()
        fillData()
    }

    private fun getData() {
        name = resources.getStringArray(R.array.name)
        username = resources.getStringArray(R.array.username)
        location = resources.getStringArray(R.array.location)
        repository = resources.getIntArray(R.array.repository)
        company = resources.getStringArray(R.array.company)
        followers = resources.getIntArray(R.array.followers)
        following = resources.getIntArray(R.array.following)
        avatar = resources.obtainTypedArray(R.array.avatar)
    }

    private fun fillData() {
        for (i in name.indices) {
            val user = User(
                username[i],
                name[i],
                location[i],
                repository[i],
                company[i],
                followers[i],
                following[i],
                avatar.getResourceId(i, -1)
            )
            users.add(user)
        }
    }

}