package com.example.familybudgetmanager.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.example.familybudgetmanager.R
import com.example.familybudgetmanager.databinding.FragmentNavhostProfileBinding
import com.example.familybudgetmanager.BaseDataBindingFragment

class ProfileNavHostFragment: BaseDataBindingFragment<FragmentNavhostProfileBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_navhost_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(
            R.id.nested_nav_host_fragment_profile) as NavHostFragment
        val navController = navHostFragment.navController
    }
}