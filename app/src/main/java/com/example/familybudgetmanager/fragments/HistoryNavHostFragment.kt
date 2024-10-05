package com.example.familybudgetmanager.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.example.familybudgetmanager.R
import com.example.familybudgetmanager.databinding.FragmentNavhostHistoryBinding
import com.example.familybudgetmanager.BaseDataBindingFragment

class HistoryNavHostFragment: BaseDataBindingFragment<FragmentNavhostHistoryBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_navhost_history

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(
            R.id.nested_nav_host_fragment_history) as NavHostFragment
        val navController = navHostFragment.navController
    }
}