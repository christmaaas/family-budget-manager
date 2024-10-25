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
import com.example.familybudgetmanager.adapters.BudgetAdapter
import com.example.familybudgetmanager.databinding.FragmentHomeBinding
import com.example.familybudgetmanager.models.Budget
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import java.util.Locale

class Home : Fragment(), BudgetAdapter.RecyclerViewEvent {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentHomeBinding is null")

    private val args: HomeArgs by navArgs()

    private lateinit var budgetAdapter: BudgetAdapter
    private val budgetList = mutableListOf<Budget>()

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "budget_history_prefs"
    private val BUDGET_LIST_KEY = "budget_list"
    private val BUDGET_KEY = "last_budget"
    private val PERIOD_KEY = "last_period"
    private val PERIOD_TYPE_KEY = "last_period_type"
    private val EXPENSE_KEY = "last_expense"
    private val INCOME_KEY = "last_income"
    private val USER_PREFS = "user_prefs"
    private val USERNAME_KEY = "username"
    private val NEW_BUDGET_KEY = "new_budget"
    private val PREFS_NAME_TRANSACTIONS = "transactions_prefs"
    private val TRANSACTION_LIST_KEY = "transactions_list"

    private val currency = "BYN"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        loadLastBudgetAndPeriod(currency)
        loadBudgetHistory()

        budgetAdapter = BudgetAdapter(budgetList, this)
        binding.budgetRecyclerView.adapter = budgetAdapter
        binding.budgetRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.setBudgetButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_setBudgetFragment)
        }

        try {
            if (args.budget.isNotEmpty() && args.period.isNotEmpty() && args.periodType.isNotEmpty()) {
                val newBudget = Budget(
                    budgetAmount = args.budget + currency,
                    budgetPeriodType = "Period: ${args.period} ${args.periodType}",
                    budgetDateTitle = getCurrentDate(),
                    userNameTitle = loadUserName()
                )

                saveLastBudgetAndPeriod(args.budget, args.period, args.periodType)
                saveLastExpenseAndIncome("0.0", "0.0")
                loadLastBudgetAndPeriod(currency)

                clearCopiedTransactionList()

                budgetList.add(newBudget)
                saveBudgetHistory()

                budgetAdapter.notifyItemInserted(budgetList.size - 1)

                Toast.makeText(requireContext(), "New budget setted. Transactions history cleared", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        loadLastBudgetAndPeriod(currency)
    }

    private fun saveLastBudgetAndPeriod(budget: String, period: String, periodType: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BUDGET_KEY, budget)
        editor.putString(PERIOD_KEY, period)
        editor.putString(PERIOD_TYPE_KEY, periodType)
        editor.putString(NEW_BUDGET_KEY, budget)
        editor.apply()
    }

    private fun saveLastExpenseAndIncome(expense: String, income: String) {
        val editor = sharedPreferences.edit()
        editor.putString(EXPENSE_KEY, expense)
        editor.putString(INCOME_KEY, income)
        editor.apply()
    }

    private fun loadLastBudgetAndPeriod(currency: String) {
        val budget = sharedPreferences.getString(BUDGET_KEY, "Not Selected")
        val period = sharedPreferences.getString(PERIOD_KEY, "Not Selected")
        val periodType = sharedPreferences.getString(PERIOD_TYPE_KEY, "")

        val expense = sharedPreferences.getString(EXPENSE_KEY, "0.0")
        val income = sharedPreferences.getString(INCOME_KEY, "0.0")

        binding.budget.text = budget + currency
        binding.period.text = "Period: $period $periodType"
        binding.expense.text = expense + currency
        binding.income.text = income + currency
    }

    private fun saveBudgetHistory() {
        val gson = Gson()
        val json = gson.toJson(budgetList)
        sharedPreferences.edit().putString(BUDGET_LIST_KEY, json).apply()
    }

    private fun loadBudgetHistory() {
        val gson = Gson()
        val json = sharedPreferences.getString(BUDGET_LIST_KEY, null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Budget>>() {}.type
            val loadedBudgets: MutableList<Budget> = gson.fromJson(json, type)
            budgetList.clear()
            budgetList.addAll(loadedBudgets)
        }
    }

    private fun loadUserName(): String {
        val userPrefs = requireContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        return userPrefs.getString(USERNAME_KEY, "User") ?: "User"
    }

    private fun clearCopiedTransactionList() {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME_TRANSACTIONS, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.remove(TRANSACTION_LIST_KEY)
        editor.apply()
    }

    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onDeleteBudget(budget: Budget) {
        val position = budgetList.indexOf(budget)
        if (position != -1) {
            budgetList.removeAt(position)
            saveBudgetHistory()
            budgetAdapter.notifyItemRemoved(position)
        }
        Toast.makeText(requireContext(), "Budget deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(budget: Budget) {
        // TODO
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
