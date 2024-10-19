package com.example.familybudgetmanager.fragments

import android.app.AlertDialog
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
import com.example.familybudgetmanager.adapters.TransactionAdapter
import com.example.familybudgetmanager.databinding.FragmentHomeBinding
import com.example.familybudgetmanager.models.Transaction

class Home : Fragment(), TransactionAdapter.RecyclerViewEvent {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentHomeBinding is null")

    private lateinit var transactionList: List<Transaction>

    private val args: HomeArgs by navArgs()

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "budget_prefs"
    private val BUDGET_KEY = "budget"
    private val PERIOD_KEY = "period"
    private val PERIOD_TYPE_KEY = "period_type"

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

        // Загружаем данные бюджета и периода из SharedPreferences
        loadBudgetAndPeriod(currency)

        binding.setBudgetButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_setBudgetFragment)
        }

        // Пример списка транзакций
        transactionList = listOf(
            Transaction("Grocery", "Food", 50.0, "2023-10-04", "Spending"),
            Transaction("Rent", "Housing", 500.0, "2023-10-01", "Spending"),
            Transaction("Gym", "Health", 30.0, "2023-10-02", "Spending")
        )

        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        //binding.transactionRecyclerView.adapter = TransactionAdapter(transactionList, this)

        try {
            if (args.budget.isNotEmpty() && args.period.isNotEmpty() && args.periodType.isNotEmpty()) {
                saveBudgetAndPeriod(args.budget, args.period, args.periodType)
                loadBudgetAndPeriod(currency)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveBudgetAndPeriod(budget: String, period: String, periodType: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BUDGET_KEY, budget)
        editor.putString(PERIOD_KEY, period)
        editor.putString(PERIOD_TYPE_KEY, periodType)
        editor.apply() // Сохраняем изменения
    }

    private fun loadBudgetAndPeriod(currency: String) {
        val budget = sharedPreferences.getString(BUDGET_KEY, "Not Selected")
        val period = sharedPreferences.getString(PERIOD_KEY, "Not Selected")
        val periodType = sharedPreferences.getString(PERIOD_TYPE_KEY, "")

        // Устанавливаем значения в TextView
        binding.budget.text = budget + currency
        binding.period.text = "Period: $period $periodType"
    }

    override fun onDeleteTransaction(transaction: Transaction) {
//        // Устанавливаем выбранную позицию для последующего удаления
//        selectedTransactionPosition = position
//
//        // Открываем контекстное меню (если нужно вручную)
//        requireActivity().openContextMenu(binding.transactionsRecyclerView)
    }

    private fun deleteTransaction(position: Int) {
//        // Удаляем транзакцию из списка
//        transactionsList.removeAt(position)
//
//        // Сохраняем изменения в SharedPreferences
//        saveTransactions()
//
//        // Уведомляем адаптер о том, что элемент удален
//        transactionsAdapter.notifyItemRemoved(position)
    }

    override fun onItemClick(transaction: Transaction) {
        // Обработка клика по транзакции (по желанию)
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


