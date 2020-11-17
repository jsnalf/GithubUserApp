package com.vanta.githubuserapp.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vanta.githubuserapp.R
import com.vanta.githubuserapp.fragments.UserFollowListFragment

class ViewPagerAdapter(val mContext: Context,fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    private val TAB_TITLES = intArrayOf(
        R.string.following_lbl,
        R.string.followers_lbl
    )

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        val fragment = UserFollowListFragment.newInstance(username, position + 1)
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title = mContext.resources.getString(TAB_TITLES[position])
        return title
    }
}