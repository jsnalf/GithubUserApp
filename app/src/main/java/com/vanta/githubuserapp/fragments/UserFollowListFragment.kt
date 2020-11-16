package com.vanta.githubuserapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.vanta.githubuserapp.Constants
import com.vanta.githubuserapp.R
import com.vanta.githubuserapp.activities.UserDetailActivity
import com.vanta.githubuserapp.adapters.ListUserAdapter
import com.vanta.githubuserapp.models.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_user_follow_list.*
import org.json.JSONArray
import java.io.OutputStreamWriter
import java.lang.Exception

class UserFollowListFragment : Fragment() {

    private val client = AsyncHttpClient()
    private var users = arrayListOf<User>()
    private var followings = arrayListOf<User>()
    private var followers = arrayListOf<User>()

    companion object {
        private val INDEX_NUMBER = "index_number"
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?, index: Int) : UserFollowListFragment {
            val fragment = UserFollowListFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            bundle.putInt(INDEX_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        client.apply {
            addHeader("Authorization", "token ${Constants.GITHUB_TOKEN}")
            addHeader("User-Agent", "request")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_follow_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_follow.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, (layoutManager as LinearLayoutManager).orientation))
        }

        val listUserAdapter = ListUserAdapter(users)
        rv_follow.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                showUserDetail(user)
            }
        })

        var index = 1
        var username = ""

        if (arguments != null) {
            username = arguments?.getString(ARG_USERNAME) as String
            index = arguments?.getInt(INDEX_NUMBER, 0) as Int
        }

        when (index) {
            1 -> getGithubUserFollowingsList(username)
            2 -> getGithubUserFollowersList(username)
        }

    }

    private fun showUserDetail(user: User) {
        val userDetailIntent = Intent(activity, UserDetailActivity::class.java)
        userDetailIntent.putExtra("USER_INFO", user)
        startActivity(userDetailIntent)
    }

    fun getGithubUserFollowingsList(usernameParam: String) {
        client.get(
            Constants.GITHUB_LIST_FOLLOWING_URL.replace("{username}", usernameParam),
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    var result = responseBody?.let { String(it) }
                    try {
                        val responseObj = result?.let { JSONArray(it) }
                        if (responseObj != null) {
                            for (i in 0 until responseObj.length()) {
                                val jsonObject = responseObj.getJSONObject(i)
                                val username = jsonObject?.getString("login")
                                val avatar = jsonObject?.getString("avatar_url")
                                val user = User(username = username, avatar = avatar)
                                users.add(user)
                            }
                            rv_follow.adapter?.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(activity, "Get Followings Failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    fun getGithubUserFollowersList(usernameParam: String) {
        client.get(
            Constants.GITHUB_LIST_FOLLOWER_URL.replace("{username}", usernameParam),
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    var result = responseBody?.let { String(it) }
                    try {
                        val responseObj = result?.let { JSONArray(it) }
                        if (responseObj != null) {
                            for (i in 0 until responseObj.length()) {
                                val jsonObject = responseObj.getJSONObject(i)
                                val username = jsonObject?.getString("login")
                                val avatar = jsonObject?.getString("avatar_url")
                                val user = User(username = username, avatar = avatar)
                                users.add(user)
                            }
                            rv_follow.adapter?.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(activity, "Get Followers Failed", Toast.LENGTH_LONG).show()
                }
            })
    }

}