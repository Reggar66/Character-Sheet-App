package com.awkwardlydevelopedapps.unicharsheet.stats.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.awkwardlydevelopedapps.unicharsheet.stats.StatsPageFragment

class StatTabsAdapter(
    private val context: Context,
    fragmentActivity: FragmentActivity,
    private var NUMBER_OF_PAGES: Int
) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentTitleList: ArrayList<String> = ArrayList()

    companion object {
        const val KEY_NUMBER_OF_TABS = "NUMBER_OF_TABS"
        const val KEY_TAB_NAME = "TAB_NAME"
    }

    init {
        loadTabNames()
    }

    override fun getItemCount(): Int {
        return NUMBER_OF_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        val statPage = StatsPageFragment()
        statPage.setPage(position + 1)
        return statPage
    }

    fun addPage() {
        NUMBER_OF_PAGES++
        val title = "Stats " + (NUMBER_OF_PAGES - 1)
        fragmentTitleList.add(title)
        saveNumberOfPages()
        notifyDataSetChanged()
    }

    fun removePage() {
        if (NUMBER_OF_PAGES > 3) {
            fragmentTitleList.removeLast()
            NUMBER_OF_PAGES--
            saveNumberOfPages()
            notifyDataSetChanged()
        }
    }

    /**
     * Saves current number of pages in shared preferences.
     */
    private fun saveNumberOfPages() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sharedPrefEditor = sharedPreferences.edit()
        sharedPrefEditor.putString(KEY_NUMBER_OF_TABS, NUMBER_OF_PAGES.toString())
        sharedPrefEditor.apply()
    }

    /**
     * Loads title list from shared preferences and stores them in fragmentTitleList
     */
    private fun loadTabNames() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        for (i in 0 until NUMBER_OF_PAGES) {
            sharedPreferences.getString(KEY_TAB_NAME + i, "STATS $i")?.let {
                fragmentTitleList.add(
                    it
                )
            }
        }
    }

    fun getTitle(i: Int): String {
        return fragmentTitleList[i]
    }

    fun toStringTitleList(): String {
        var str = "Titles: "

        fragmentTitleList.forEach {
            str += "\n$it"
        }

        return str
    }
}