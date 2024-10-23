package com.example.familybudgetmanager.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familybudgetmanager.R
import com.example.familybudgetmanager.adapters.TransactionAdapter
import com.example.familybudgetmanager.databinding.FragmentHistoryBinding
import com.example.familybudgetmanager.models.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class History : Fragment(), TransactionAdapter.RecyclerViewEvent {
    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentHistoryBinding is null")

    private val args: HistoryArgs by navArgs()

    private lateinit var transactionsAdapter: TransactionAdapter
    private val transactionsList = mutableListOf<Transaction>()

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "transactions_prefs"
    private val TRANSACTION_LIST_KEY = "transactions_list"
    private val USER_PREFS = "user_prefs"
    private val USERNAME_KEY = "username"

    private var currentFilter: String? = null

    private val currency = "BYN"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        loadTransactions()

        transactionsAdapter = TransactionAdapter(transactionsList, this)
        binding.transactionsRecyclerView.adapter = transactionsAdapter
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(context)

        try {
            if (args.title.isNotEmpty() && args.category.isNotEmpty() && args.amount.isNotEmpty() && args.description.isNotEmpty() && args.transactionType.isNotEmpty()) {
                val transactionAmountVal = if (args.transactionType == "Profit") {
                    "+" + args.amount + currency
                } else {
                    "-" + args.amount + currency
                }

                val transaction = Transaction(
                    args.title,
                    loadUserName(),
                    args.category,
                    args.description,
                    transactionAmountVal,
                    getCurrentDate(),
                    args.transactionType
                )
                transactionsList.add(transaction)

                saveTransactions()

                transactionsAdapter.notifyItemInserted(transactionsList.size - 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.transactionTypeToggleGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                binding.spendingToggleSwitchButton.id -> filterTransactions("Spending")
                binding.profitToggleSwitchButton.id -> filterTransactions("Profit")
            }
        }

        binding.addNewTransactionFloatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_history_to_addTransactionFragment)
        }
    }

    private fun saveTransactions() {
        val gson = Gson()
        val json = gson.toJson(transactionsList)
        sharedPreferences.edit().putString(TRANSACTION_LIST_KEY, json).apply()
    }

    private fun loadTransactions() {
        val gson = Gson()
        val json = sharedPreferences.getString(TRANSACTION_LIST_KEY, null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Transaction>>() {}.type
            val loadedTransactions: MutableList<Transaction> = gson.fromJson(json, type)
            transactionsList.clear()
            transactionsList.addAll(loadedTransactions)
        }
    }

    private fun loadUserName(): String {
        val userPrefs = requireContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        return userPrefs.getString(USERNAME_KEY, "User") ?: "User"
    }

    private fun getCurrentDate(): String {
        return "19.10.2024"
    }

    override fun onItemClick(transaction: Transaction) {
        // TODO
    }

    private fun filterTransactions(type: String) {
        currentFilter = type
        val filteredList = transactionsList.filter { it.type == type }
        transactionsAdapter = TransactionAdapter(filteredList.toMutableList(), this)
        binding.transactionsRecyclerView.adapter = transactionsAdapter
    }

    override fun onDeleteTransaction(transaction: Transaction) {
        val positionInMainList = transactionsList.indexOf(transaction)
        if (positionInMainList != -1) {
            transactionsList.removeAt(positionInMainList)
            saveTransactions()

            currentFilter?.let {
                filterTransactions(it)
            } ?: transactionsAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
