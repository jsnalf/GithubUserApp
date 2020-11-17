package com.vanta.githubuserapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.vanta.githubuserapp.Constants
import com.vanta.githubuserapp.R
import com.vanta.githubuserapp.adapters.ViewPagerAdapter
import com.vanta.githubuserapp.models.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.json.JSONObject

class UserDetailActivity : AppCompatActivity() {

    private val client = AsyncHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        supportActionBar?.title = getString(R.string.app_user_detail_name)

        client.apply {
            addHeader("Authorization", "token ${Constants.GITHUB_TOKEN}")
            addHeader("User-Agent", "request")
        }

        val user = intent.getParcelableExtra<User>("USER_INFO")

        Glide.with(this)
            .load(user?.avatar)
            .override(100, 100)
            .centerCrop()
            .into(iv_avatar)

        tv_username.text = user?.username
        getGithubUserDetail(user?.username)

        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        viewPagerAdapter.username = user?.username
        view_pager.adapter = viewPagerAdapter
        tab_layout.setupWithViewPager(view_pager)

    }

    fun getGithubUserDetail(username: String?) {

        detailUserProgressBar.visibility = View.VISIBLE

        if (username != null) {
            client.get(
                Constants.GITHUB_DETAIL_USER_URL.replace("{username}", username),
                object : AsyncHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?
                    ) {

                        detailUserProgressBar.visibility = View.INVISIBLE
                        var result = responseBody?.let { JSONObject(String(it)) }

                        if (result != null) {
                            val name = if (result.isNull("name")) "-" else result.getString("name")
                            val location = if (result.isNull("location")) "-" else result.getString("location")
                            val company = if (result.isNull("company")) "-" else result.getString("company")
                            val repos_count = if (result.isNull("public_repos")) "-" else result.getInt("public_repos")
                            val followers_count = if (result.isNull("followers")) "-" else result.getInt("followers")
                            val following_count = if (result.isNull("following")) "-" else result.getInt("following")

                            tv_name.text = name
                            tv_location.text = location
                            tv_company.text = company
                            tv_repository.text = repos_count.toString()
                            tv_followers.text = followers_count.toString()
                            tv_following.text = following_count.toString()
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

}