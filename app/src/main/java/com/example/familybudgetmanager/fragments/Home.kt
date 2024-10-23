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
import com.example.familybudgetmanager.adapters.BudgetAdapter
import com.example.familybudgetmanager.databinding.FragmentHomeBinding
import com.example.familybudgetmanager.models.Budget
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Home : Fragment(), BudgetAdapter.RecyclerViewEvent {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentHomeBinding is null")

    private val args: HomeArgs by navArgs()

    private lateinit var budgetAdapter: BudgetAdapter
    private val budgetList = mutableListOf<Budget>() // Список для истории изменений бюджета

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "budget_history_prefs"
    private val BUDGET_LIST_KEY = "budget_list"
    private val BUDGET_KEY = "last_budget"
    private val PERIOD_KEY = "last_period"
    private val PERIOD_TYPE_KEY = "last_period_type"
    // Добавляем ключи для хранения расходов и доходов
    private val EXPENSE_KEY = "last_expense"
    private val INCOME_KEY = "last_income"

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

        val currency = "$"

        // Загружаем последнее значение бюджета, периода, расходов и доходов из SharedPreferences
        loadLastBudgetAndPeriod(currency)

        // Загружаем историю изменений бюджета при запуске фрагмента
        loadBudgetHistory()

        // Настраиваем адаптер для RecyclerView
        budgetAdapter = BudgetAdapter(budgetList, this)
        binding.budgetRecyclerView.adapter = budgetAdapter
        binding.budgetRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.setBudgetButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_setBudgetFragment)
        }

        try {
            if (args.budget.isNotEmpty() && args.period.isNotEmpty() && args.periodType.isNotEmpty()) {
                // Создаем новый объект бюджета на основе переданных аргументов
                val newBudget = Budget(
                    budgetAmount = args.budget + currency,
                    budgetPeriodType = "Period: ${args.period} ${args.periodType}",
                    budgetDateTitle = getCurrentDate(),
                    userNameTitle = "User" // Можно заменить на реальное имя пользователя
                )

                // Сохраняем последний бюджет, период, расходы и доходы в SharedPreferences
                saveLastBudgetAndPeriod(args.budget, args.period, args.periodType)
                saveLastExpenseAndIncome("0.0", "0.0") // Сохраняем начальные значения расходов и доходов
                loadLastBudgetAndPeriod(currency) // Обновляем отображение

                // Добавляем новый бюджет в список истории
                budgetList.add(newBudget)
                saveBudgetHistory() // Сохраняем изменения в SharedPreferences

                // Уведомляем адаптер о добавлении нового элемента
                budgetAdapter.notifyItemInserted(budgetList.size - 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем UI при каждом возврате к фрагменту
        val currency = "$"
        loadLastBudgetAndPeriod(currency)
    }

    // Сохраняем последний установленный бюджет и период
    private fun saveLastBudgetAndPeriod(budget: String, period: String, periodType: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BUDGET_KEY, budget)
        editor.putString(PERIOD_KEY, period)
        editor.putString(PERIOD_TYPE_KEY, periodType)
        editor.apply() // Сохраняем изменения
    }

    // Сохраняем последние значения расходов и доходов
    private fun saveLastExpenseAndIncome(expense: String, income: String) {
        val editor = sharedPreferences.edit()
        editor.putString(EXPENSE_KEY, expense)
        editor.putString(INCOME_KEY, income)
        editor.apply()
    }

    // Загружаем последнее значение бюджета, периода, расходов и доходов
    private fun loadLastBudgetAndPeriod(currency: String) {
        val budget = sharedPreferences.getString(BUDGET_KEY, "Not Selected")
        val period = sharedPreferences.getString(PERIOD_KEY, "Not Selected")
        val periodType = sharedPreferences.getString(PERIOD_TYPE_KEY, "")

        val expense = sharedPreferences.getString(EXPENSE_KEY, "0.0")
        val income = sharedPreferences.getString(INCOME_KEY, "0.0")

        // Устанавливаем значения на экран
        binding.budget.text = budget + currency
        binding.period.text = "Period: $period $periodType"
        binding.expense.text = expense + currency
        binding.income.text = income + currency
    }

    // Сохраняем историю изменений бюджета в SharedPreferences
    private fun saveBudgetHistory() {
        val gson = Gson()
        val json = gson.toJson(budgetList) // Преобразуем список в JSON
        sharedPreferences.edit().putString(BUDGET_LIST_KEY, json).apply() // Сохраняем JSON в SharedPreferences
    }

    // Загружаем историю изменений бюджета из SharedPreferences
    private fun loadBudgetHistory() {
        val gson = Gson()
        val json = sharedPreferences.getString(BUDGET_LIST_KEY, null) // Получаем JSON из SharedPreferences
        if (json != null) {
            val type = object : TypeToken<MutableList<Budget>>() {}.type
            val loadedBudgets: MutableList<Budget> = gson.fromJson(json, type) // Преобразуем JSON обратно в список
            budgetList.clear()
            budgetList.addAll(loadedBudgets) // Добавляем загруженные бюджеты в список
        }
    }

    private fun getCurrentDate(): String {
        // Возвращаем текущую дату в нужном формате (замени на свою реализацию)
        return "19.10.2024" // Пример
    }

    // Реализуем метод для обработки удаления бюджета
    override fun onDeleteBudget(budget: Budget) {
        // Удаляем бюджет из списка
        val position = budgetList.indexOf(budget)
        if (position != -1) {
            budgetList.removeAt(position)
            saveBudgetHistory() // Сохраняем изменения в SharedPreferences
            budgetAdapter.notifyItemRemoved(position)
        }
    }

    // Обработка клика по элементу бюджета (по желанию, можешь изменить логику)
    override fun onItemClick(budget: Budget) {
        // Обработка клика по элементу списка
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
