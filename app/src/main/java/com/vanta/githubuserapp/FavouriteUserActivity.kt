package com.vanta.githubuserapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanta.githubuserapp.R
import com.vanta.githubuserapp.adapters.ListUserAdapter
import com.vanta.githubuserapp.db.UserHelper
import com.vanta.githubuserapp.models.User
import kotlinx.android.synthetic.main.activity_favourite_user.*

class FavouriteUserActivity : AppCompatActivity() {

    private lateinit var userHelper: UserHelper
    private var favoriteUsers = arrayListOf<User>()
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_user)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        val layoutManager = LinearLayoutManager(this)
        rv_favorite_users.layoutManager = layoutManager

        val divider = DividerItemDecoration(rv_favorite_users.context, layoutManager.orientation)
        rv_favorite_users.addItemDecoration(divider)

        listUserAdapter = ListUserAdapter(favoriteUsers)
        rv_favorite_users.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                showUserDetail(user)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        getFavoriteUsers()
        listUserAdapter.notifyDataSetChanged()
    }

    private fun getFavoriteUsers() {
        favoriteUsers.clear()
        val cursor = userHelper.queryAll()
        cursor.apply {
            while (moveToNext()) {
                val avatar_url = getString(getColumnIndex("avatar_url"))
                val username = getString(getColumnIndex("username"))
                favoriteUsers.add(User(username = username, avatar = avatar_url))
            }
        }
    }

    private fun showUserDetail(user: User) {
        val userDetailIntent = Intent(this, UserDetailActivity::class.java)
        userDetailIntent.putExtra(UserDetailActivity.USER_INFO, user)
        startActivity(userDetailIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.favorite_users -> {
                startActivity(Intent(this, FavouriteUserActivity::class.java))
                true
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }

}