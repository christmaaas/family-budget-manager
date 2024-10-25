package com.example.familybudgetmanager.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.util.Date
import java.util.Locale

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
    private val PREFS_NAME_BUDGET = "budget_history_prefs"
    private val BUDGET_KEY = "last_budget"
    private val EXPENSE_KEY = "last_expense"
    private val INCOME_KEY = "last_income"

    private var currentFilter: String? = null

    private val currency = "BYN"

    companion object {
        init {
            System.loadLibrary("budget_calculator")
        }
    }

    external fun calculateNewBudget(currentBudget: Double, amount: Double, isProfit: Boolean): String
    external fun updateIncomeOrExpense(currentIncome: Double, currentExpense: Double, amount: Double, isProfit: Boolean): String

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

                Toast.makeText(requireContext(), "New transaction added", Toast.LENGTH_SHORT).show()
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
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onItemClick(transaction: Transaction) {
        val action = HistoryDirections.actionHistoryToTransactionDetailsFragment(
            transaction.title,
            transaction.category,
            transaction.amount,
            transaction.description,
            transaction.date,
            transaction.username
        )
        findNavController().navigate(action)
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

            sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME_BUDGET, Context.MODE_PRIVATE)

            val currentBudget = sharedPreferences.getString(BUDGET_KEY, "0.0")?.toDouble() ?: 0.0
            val currentExpense = sharedPreferences.getString(EXPENSE_KEY, "0.0")?.toDouble() ?: 0.0
            val currentIncome = sharedPreferences.getString(INCOME_KEY, "0.0")?.toDouble() ?: 0.0

            val isProfit = transaction.type == "Profit"

            val transactionAmount = transaction.amount.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0

            val newBudget = calculateNewBudget(currentBudget, transactionAmount, isProfit)
            val updatedValue = updateIncomeOrExpense(currentIncome, currentExpense, transactionAmount, isProfit)

            if (isProfit) {
                sharedPreferences.edit().putString(INCOME_KEY, updatedValue).apply()
            } else {
                sharedPreferences.edit().putString(EXPENSE_KEY, updatedValue).apply()
            }

            sharedPreferences.edit().putString(BUDGET_KEY, newBudget).apply()

            sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

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
