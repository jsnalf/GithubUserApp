package com.vanta.githubuserapp.activities

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.vanta.githubuserapp.Constants
import com.vanta.githubuserapp.adapters.ListUserAdapter
import com.vanta.githubuserapp.R
import com.vanta.githubuserapp.models.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var rvUser: RecyclerView
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchValue: String?): Boolean {
                users.clear()
                rvUser.adapter?.notifyDataSetChanged()
                searchValue?.let { searchGithubUser(it) }
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

    }

    fun searchGithubUser(searchValue: String) {
        listUserProgressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${Constants.GITHUB_TOKEN}")
        client.addHeader("User-Agent", "request")
        client.get(Constants.GITHUB_SEARCH_URL.replace("{username}", searchValue),
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    listUserProgressBar.visibility = View.INVISIBLE
                    var result = responseBody?.let { String(it) }
                    try {
                        val responseObj = result?.let { JSONObject(it) }
                        if (responseObj != null) {
                            val searchResults = responseObj.getJSONArray("items")
                            for (i in 0 until searchResults.length()) {
                                val jsonObject = searchResults.getJSONObject(i)
                                val username = jsonObject?.getString("login")
                                val avatar = jsonObject?.getString("avatar_url")
                                val user = User(username = username, avatar = avatar)
                                users.add(user)
                            }
                            rvUser.adapter?.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    listUserProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@MainActivity, "Search User Failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun showUserDetail(user: User) {
        val userDetailIntent = Intent(this, UserDetailActivity::class.java)
        userDetailIntent.putExtra("USER_INFO", user)
        startActivity(userDetailIntent)
    }


}