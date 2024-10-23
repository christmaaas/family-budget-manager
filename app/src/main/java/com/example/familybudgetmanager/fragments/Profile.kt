package com.example.familybudgetmanager.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.familybudgetmanager.auth.SignInActivity
import com.example.familybudgetmanager.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class Profile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentProfileBinding is null")

    private lateinit var firebaseAuth: FirebaseAuth

    private val USER_PREFS = "user_prefs"
    private val USERNAME_KEY = "username"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(USERNAME_KEY, "User")

        binding.userNameLabel.text = username

        firebaseAuth = FirebaseAuth.getInstance()

        binding.logoutLabel.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
