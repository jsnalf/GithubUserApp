package com.vanta.githubuserapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanta.githubuserapp.adapters.ListUserAdapter
import com.vanta.githubuserapp.R
import com.vanta.githubuserapp.models.User
import com.vanta.githubuserapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var rvUser: RecyclerView
    private var users = arrayListOf<User>()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchValue: String?): Boolean {
                rvUser.visibility = View.INVISIBLE
                listUserProgressBar.visibility = View.VISIBLE
                searchValue?.let { mainViewModel.setUser(it) }
                return true
            }

            override fun onQueryTextChange(searchValue: String?): Boolean {
                return false
            }

        })

        rvUser = findViewById(R.id.rv_user)
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

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                listUserAdapter.setData(userItems)
                listUserProgressBar.visibility = View.INVISIBLE
                rvUser.visibility = View.VISIBLE
            }
        })

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

    private fun showUserDetail(user: User) {
        val userDetailIntent = Intent(this, UserDetailActivity::class.java)
        userDetailIntent.putExtra(UserDetailActivity.USER_INFO, user)
        startActivity(userDetailIntent)
    }

}