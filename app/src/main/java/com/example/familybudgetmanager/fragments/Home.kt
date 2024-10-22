package com.example.familybudgetmanager.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // Загружаем последнее значение бюджета и периода из SharedPreferences
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
                    budgetAmount = args.budget,
                    budgetPeriodType = "Period: ${args.period} ${args.periodType}",
                    budgetDateTitle = getCurrentDate(),
                    userNameTitle = "User" // Можно заменить на реальное имя пользователя
                )

                // Сохраняем последний бюджет и период в SharedPreferences
                saveLastBudgetAndPeriod(args.budget, args.period, args.periodType)
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

    // Сохраняем последний установленный бюджет и период
    private fun saveLastBudgetAndPeriod(budget: String, period: String, periodType: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BUDGET_KEY, budget)
        editor.putString(PERIOD_KEY, period)
        editor.putString(PERIOD_TYPE_KEY, periodType)
        editor.apply() // Сохраняем изменения
    }

    // Загружаем последнее значение бюджета и периода
    private fun loadLastBudgetAndPeriod(currency: String) {
        val budget = sharedPreferences.getString(BUDGET_KEY, "Not Selected")
        val period = sharedPreferences.getString(PERIOD_KEY, "Not Selected")
        val periodType = sharedPreferences.getString(PERIOD_TYPE_KEY, "")

        // Устанавливаем значения на экран
        binding.budget.text = budget + currency
        binding.period.text = "Period: $period $periodType"
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
