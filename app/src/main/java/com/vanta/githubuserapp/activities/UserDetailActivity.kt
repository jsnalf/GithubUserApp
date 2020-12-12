package com.vanta.githubuserapp.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.vanta.githubuserapp.Constants
import com.vanta.githubuserapp.R
import com.vanta.githubuserapp.adapters.ViewPagerAdapter
import com.vanta.githubuserapp.db.DatabaseContract
import com.vanta.githubuserapp.db.UserHelper
import com.vanta.githubuserapp.models.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.json.JSONObject

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    private val client = AsyncHttpClient()
    private lateinit var userHelper: UserHelper
    private lateinit var user: User
    private var favoriteStatus: Boolean = false

    companion object {
        const val USER_INFO = "user_info"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        supportActionBar?.title = getString(R.string.app_user_detail_name)

        client.apply {
            addHeader("Authorization", "token ${Constants.GITHUB_TOKEN}")
            addHeader("User-Agent", "request")
        }

        val userIntent = intent.getParcelableExtra<User>(USER_INFO)

        user = User(
            userIntent?.username,
            "-",
            "-",
            0,
            "-",
            0,
            0,
            userIntent?.avatar
        )

        Glide.with(this)
            .load(user.avatar)
            .override(100, 100)
            .centerCrop()
            .into(iv_avatar)

        tv_username.text = user.username
        getGithubUserDetail(user.username)

        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        viewPagerAdapter.username = user.username
        view_pager.adapter = viewPagerAdapter
        tab_layout.setupWithViewPager(view_pager)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        favoriteStatus = isUserFavourited(user.username)
        setFavoriteStatus(favoriteStatus)
        fab_favorite.setOnClickListener(this)

    }

    fun getGithubUserDetail(username: String?) {

        if (username != null) {
            client.get(
                Constants.GITHUB_DETAIL_USER_URL.replace("{username}", username),
                object : AsyncHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?
                    ) {

                        rlayout.visibility = View.GONE
                        var result = responseBody?.let { JSONObject(String(it)) }

                        if (result != null) {
                            val result_name = if (result.isNull("name")) "-" else result.getString("name")
                            val result_location = if (result.isNull("location")) "-" else result.getString("location")
                            val result_company = if (result.isNull("company")) "-" else result.getString("company")
                            val result_repos_count = result.getInt("public_repos")
                            val result_followers_count = result.getInt("followers")
                            val result_following_count = result.getInt("following")

                            user.apply {
                                name = result_name
                                location = result_location
                                repository = result_repos_count
                                company = result_company
                                followers = result_followers_count
                                following = result_following_count
                            }

                            tv_name.text = user.name
                            tv_location.text = user.location
                            tv_company.text = user.company
                            tv_repository.text = user.repository.toString()
                            tv_followers.text = user.followers.toString()
                            tv_following.text = user.following.toString()

                        }

                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?,
                        error: Throwable?
                    ) {
                        detailUserProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(
                            this@UserDetailActivity,
                            "Get User Detail Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    override fun onClick(view: View?) {

        if (favoriteStatus) {
            val deleteResult = userHelper.deleteByUsername(user.username)

            if (deleteResult > 0) {
                favoriteStatus = !favoriteStatus
                setFavoriteStatus(favoriteStatus)
                Toast.makeText(this, "User deleted from favorites", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to remove user from favorites", Toast.LENGTH_SHORT).show()
            }

        } else {

            val values = ContentValues()
            values.put(DatabaseContract.UserColumns.USERNAME, user.username)
            values.put(DatabaseContract.UserColumns.NAME, user.name)
            values.put(DatabaseContract.UserColumns.LOCATION, user.location)
            values.put(DatabaseContract.UserColumns.REPOSITORY, user.repository)
            values.put(DatabaseContract.UserColumns.COMPANY, user.company)
            values.put(DatabaseContract.UserColumns.FOLLOWERS, user.followers)
            values.put(DatabaseContract.UserColumns.FOLLOWING, user.following)
            values.put(DatabaseContract.UserColumns.AVATAR_URL, user.avatar )

            val insertResult = userHelper.insert(values)

            if (insertResult > 0) {
                favoriteStatus = !favoriteStatus
                setFavoriteStatus(favoriteStatus)
                Toast.makeText(this, "User added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add user to favorites", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setFavoriteStatus(favoriteStatus: Boolean) {
        if (favoriteStatus) {
            fab_favorite.setImageResource(R.drawable.ic_favorite_24px)
        } else {
            fab_favorite.setImageResource(R.drawable.ic_unfavorite__24px)
        }
    }

    private fun isUserFavourited(username: String?): Boolean {
        val resultCursor = username?.let { userHelper.queryByUsername(it) }
        var count = resultCursor?.count
        if (count != null) {
            return count > 0
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
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

}