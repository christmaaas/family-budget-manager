package com.example.familybudgetmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.familybudgetmanager.R
import com.example.familybudgetmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase

        setupViewPager()

        binding.viewPager.isUserInputEnabled = true

        binding.viewPager.adapter = ViewPagerAdapter(this)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navigateToPage(0)
                R.id.statistic -> navigateToPage(1)
                R.id.history -> navigateToPage(2)
                R.id.profile -> navigateToPage(3)
            }
            true
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigationView.selectedItemId = R.id.home
                    1 -> binding.bottomNavigationView.selectedItemId = R.id.statistic
                    2 -> binding.bottomNavigationView.selectedItemId = R.id.history
                    3 -> binding.bottomNavigationView.selectedItemId = R.id.profile
                }
            }
        })
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.isUserInputEnabled = true
    }

    private fun navigateToPage(pageIndex: Int) {
        if (binding.flContainer.visibility == View.VISIBLE) {
            binding.flContainer.visibility = View.GONE
        }
        viewPager.setCurrentItem(pageIndex, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
