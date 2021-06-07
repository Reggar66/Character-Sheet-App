package com.awkwardlydevelopedapps.unicharsheet.inventory.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.BackpackFragment
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.EquipmentFragment
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.PocketFragment

class InventoryTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        private const val NUMBER_OF_TABS: Int = 3
    }

    override fun getItemCount(): Int {
        return NUMBER_OF_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EquipmentFragment()
            1 -> PocketFragment()
            2 -> BackpackFragment()
            else -> EquipmentFragment()
        }
    }
}