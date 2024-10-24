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
import com.example.familybudgetmanager.databinding.FragmentAddTransactionBinding

class AddTransactionFragment : Fragment() {
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "budget_history_prefs"
    private val BUDGET_KEY = "last_budget"
    private val EXPENSE_KEY = "last_expense"
    private val INCOME_KEY = "last_income"

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
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        binding.saveTransactionButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val category = binding.categoryEditText.text.toString()
            val amountStr = binding.amountEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val selectedTransactionType = when (binding.transactionTypeRadioGroup.checkedRadioButtonId) {
                binding.spendingRadioButton.id -> "Spending"
                binding.profitRadioButton.id -> "Profit"
                else -> null
            }

            if (title.isEmpty() || category.isEmpty() || amountStr.isEmpty() || description.isEmpty() || selectedTransactionType == null) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                updateBudgetAndValues(amountStr.toDouble(), selectedTransactionType)

                val action = AddTransactionFragmentDirections.actionAddTransactionFragmentToHistoryFragment(
                    title,
                    category,
                    amountStr,
                    description,
                    selectedTransactionType
                )
                findNavController().navigate(action)
            }
        }

        binding.cancelTransactionButton.setOnClickListener {
            val action = AddTransactionFragmentDirections.actionAddTransactionFragmentToHistoryFragment(
                "",
                "",
                "",
                "",
                ""
            )
            findNavController().navigate(action)
        }
    }

    private fun updateBudgetAndValues(amount: Double, transactionType: String) {
        val editor = sharedPreferences.edit()

        val currentBudget = sharedPreferences.getString(BUDGET_KEY, "0.0")?.toDouble() ?: 0.0
        val currentExpense = sharedPreferences.getString(EXPENSE_KEY, "0.0")?.toDouble() ?: 0.0
        val currentIncome = sharedPreferences.getString(INCOME_KEY, "0.0")?.toDouble() ?: 0.0

        val isProfit = transactionType == "Profit"

        val newBudget = calculateNewBudget(currentBudget, amount, isProfit)
        val updatedValue = updateIncomeOrExpense(currentIncome, currentExpense, amount, isProfit)

        if (isProfit) {
            editor.putString(INCOME_KEY, updatedValue)
        } else {
            editor.putString(EXPENSE_KEY, updatedValue)
        }

        editor.putString(BUDGET_KEY, newBudget)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
