package com.example.familybudgetmanager.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.familybudgetmanager.R
import com.example.familybudgetmanager.databinding.FragmentStatisticBinding
import com.example.familybudgetmanager.models.Transaction
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.github.mikephil.charting.components.Description

class Statistic : Fragment() {
    private var _binding: FragmentStatisticBinding? = null
    private val binding get() = _binding!!

    private val PREFS_NAME_TRANSACTIONS = "transactions_prefs"
    private val PREFS_NAME_BUDGET = "budget_history_prefs"
    private val TRANSACTION_LIST_KEY = "transactions_list"
    private val NEW_BUDGET_KEY = "new_budget"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val transactionsList = loadCopiedTransactions()
        val initialBudget = loadInitialBudget()
        val entries = calculateBudgetEntries(transactionsList, initialBudget)
        setupLineChart(entries)
    }

    private fun loadCopiedTransactions(): MutableList<Transaction> {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME_TRANSACTIONS, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(TRANSACTION_LIST_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Transaction>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun loadInitialBudget(): Float {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME_BUDGET, Context.MODE_PRIVATE)
        val budgetString = sharedPreferences.getString(NEW_BUDGET_KEY, "0.0")
        return budgetString?.toFloat() ?: 0.0f
    }

    private fun calculateBudgetEntries(transactions: List<Transaction>, initialBudget: Float): List<Entry> {
        val entries = mutableListOf<Entry>()
        var currentBudget = initialBudget
        var timeIndex = 0f

        entries.add(Entry(timeIndex, currentBudget))
        timeIndex++

        for (transaction in transactions) {
            val amount = transaction.amount.replace("[^\\d.-]".toRegex(), "").toFloat()
            currentBudget += amount

            entries.add(Entry(timeIndex, currentBudget))
            timeIndex++
        }

        return entries
    }

    private fun setupLineChart(entries: List<Entry>) {
        val lineDataSet = LineDataSet(entries, "Budget Changes")
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawFilled(true)

        lineDataSet.fillColor = resources.getColor(R.color.colorBackground, null)

        val lineData = LineData(lineDataSet)
        binding.budgetLineChart.data = lineData

        val description = Description()
        description.text = "Budget Over Transactions"
        binding.budgetLineChart.description = description

        binding.budgetLineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
