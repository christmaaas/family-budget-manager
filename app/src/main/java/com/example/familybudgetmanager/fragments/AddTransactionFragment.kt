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
    private val BUDGET_KEY = "last_budget" // Ключ для бюджета
    private val EXPENSE_KEY = "last_expense" // Ключ для расходов
    private val INCOME_KEY = "last_income" // Ключ для доходов

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Устанавливаем слушатель клика для кнопки сохранения
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

            // Проверяем, что все поля заполнены
            if (title.isEmpty() || category.isEmpty() || amountStr.isEmpty() || description.isEmpty() || selectedTransactionType == null) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Обновляем бюджет, доход или расход
                updateBudgetAndValues(amountStr.toDouble(), selectedTransactionType)

                // Переход к HistoryFragment
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

        // Получаем текущее значение бюджета, расходов и доходов
        val currentBudget = sharedPreferences.getString(BUDGET_KEY, "0.0")?.toDouble() ?: 0.0
        val currentExpense = sharedPreferences.getString(EXPENSE_KEY, "0.0")?.toDouble() ?: 0.0
        val currentIncome = sharedPreferences.getString(INCOME_KEY, "0.0")?.toDouble() ?: 0.0

        // Обновляем бюджет в зависимости от типа транзакции
        val newBudget = if (transactionType == "Profit") {
            currentBudget + amount
        } else {
            currentBudget - amount
        }

        // Обновляем доход или расход в зависимости от типа транзакции
        if (transactionType == "Profit") {
            val newIncome = currentIncome + amount
            editor.putString(INCOME_KEY, newIncome.toString()) // Сохраняем новое значение дохода
        } else {
            val newExpense = currentExpense + amount
            editor.putString(EXPENSE_KEY, newExpense.toString()) // Сохраняем новое значение расходов
        }

        // Сохраняем обновленное значение бюджета
        editor.putString(BUDGET_KEY, newBudget.toString())
        editor.apply() // Применяем изменения
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
