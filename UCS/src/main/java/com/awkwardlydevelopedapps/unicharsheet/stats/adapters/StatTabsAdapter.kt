package com.awkwardlydevelopedapps.unicharsheet.stats.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.awkwardlydevelopedapps.unicharsheet.stats.StatsPageFragment

class StatTabsAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private val fragmentTitleList: ArrayList<String> = ArrayList()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String, position: Int) {
        fragmentList.add(position, fragment)
        fragmentTitleList.add(position, title)
    }

    fun removeFragment(position: Int) {
        fragmentList.removeAt(position)
        fragmentTitleList.removeAt(position)
    }

    fun getStatPage(i: Int): StatsPageFragment {
        return fragmentList[i] as StatsPageFragment
    }

    fun getTitle(i: Int): String {
        return fragmentTitleList[i]
    }
}