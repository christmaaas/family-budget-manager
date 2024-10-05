package com.example.familybudgetmanager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.familybudgetmanager.fragments.ProfileNavHostFragment
import com.example.familybudgetmanager.fragments.StatisticNavHostFragment
import com.example.familybudgetmanager.fragments.HomeNavHostFragment
import com.example.familybudgetmanager.fragments.HistoryNavHostFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
        HomeNavHostFragment(),
        StatisticNavHostFragment(),
        HistoryNavHostFragment(),
        ProfileNavHostFragment(),
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}