package com.example.familybudgetmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familybudgetmanager.adapters.TransactionAdapter
import com.example.familybudgetmanager.databinding.FragmentHomeBinding
import com.example.familybudgetmanager.models.Transaction

class Home : Fragment(), TransactionAdapter.RecyclerViewEvent {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentHomeBinding is null")

    private lateinit var transactionList: List<Transaction>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionList = listOf(
            Transaction("Grocery", "Food", 50.0, "2023-10-04"),
            Transaction("Rent", "Housing", 500.0, "2023-10-01"),
            Transaction("Gym", "Health", 30.0, "2023-10-02"),
            Transaction("Grocery", "Food", 50.0, "2023-10-04"),
            Transaction("Rent", "Housing", 500.0, "2023-10-01"),
            Transaction("Gym", "Health", 30.0, "2023-10-02")
        )

        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.transactionRecyclerView.adapter = TransactionAdapter(transactionList, this)

    }

    override fun onItemClick(transaction: Transaction) {
//        val action = HomeDirections.actionHomeToTransactionDetailFragment(transaction)
//        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = Home()
    }
}
