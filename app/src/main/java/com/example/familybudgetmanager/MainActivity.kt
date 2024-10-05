package com.example.familybudgetmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.familybudgetmanager.R
import com.example.familybudgetmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.isUserInputEnabled = false

        binding.viewPager.adapter = ViewPagerAdapter(this)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    binding.viewPager.setCurrentItem(0, false)
                    true
                }
                R.id.statistic -> {
                    binding.viewPager.setCurrentItem(1, false)
                    true
                }
                R.id.history -> {
                    binding.viewPager.setCurrentItem(2, false)
                    true
                }
                R.id.profile -> {
                    binding.viewPager.setCurrentItem(3, false)
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
