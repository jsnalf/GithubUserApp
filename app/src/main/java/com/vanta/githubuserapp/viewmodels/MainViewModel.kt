package com.vanta.githubuserapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.vanta.githubuserapp.Constants
import com.vanta.githubuserapp.models.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel: ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUser(searchValue: String) {
        val users = ArrayList<User>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${Constants.GITHUB_TOKEN}")
        client.addHeader("User-Agent", "request")
        client.get(
            Constants.GITHUB_SEARCH_URL.replace("{username}", searchValue),
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
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
                            listUsers.postValue(users)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Log.d("onFailure", error?.message.toString())
                }
            })
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

}