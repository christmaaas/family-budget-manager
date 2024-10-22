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
import com.example.familybudgetmanager.adapters.TransactionAdapter
import com.example.familybudgetmanager.databinding.FragmentHistoryBinding
import com.example.familybudgetmanager.models.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class History : Fragment(), TransactionAdapter.RecyclerViewEvent {
    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentHistoryBinding is null")

    private val args: HistoryArgs by navArgs()

    private lateinit var transactionsAdapter: TransactionAdapter
    private val transactionsList = mutableListOf<Transaction>() // Изменено на mutableList

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "transactions_prefs"
    private val TRANSACTION_LIST_KEY = "transactions_list"

    private var currentFilter: String? = null // Переменная для отслеживания фильтра

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

        // Загружаем данные из SharedPreferences при запуске фрагмента
        loadTransactions()

        // Настраиваем адаптер для RecyclerView
        binding.transactionsRecyclerView.adapter = TransactionAdapter(transactionsList, this)
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(context)

        try {
            if (args.title.isNotEmpty() && args.category.isNotEmpty() && args.amount.isNotEmpty() && args.description.isNotEmpty() && args.transactionType.isNotEmpty()) {
                val transaction = Transaction(args.title, args.category, args.amount.toDouble(), getCurrentDate(), args.transactionType)
                transactionsList.add(transaction)

                // Сохраняем транзакции в SharedPreferences
                saveTransactions()

                // Уведомляем адаптер о добавлении нового элемента
                transactionsAdapter.notifyItemInserted(transactionsList.size - 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Set up toggle button listener
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
        val json = gson.toJson(transactionsList) // Преобразуем список в JSON
        sharedPreferences.edit().putString(TRANSACTION_LIST_KEY, json).apply() // Сохраняем JSON в SharedPreferences
    }

    private fun loadTransactions() {
        val gson = Gson()
        val json = sharedPreferences.getString(TRANSACTION_LIST_KEY, null) // Получаем JSON из SharedPreferences
        if (json != null) {
            val type = object : TypeToken<MutableList<Transaction>>() {}.type
            val loadedTransactions: MutableList<Transaction> = gson.fromJson(json, type) // Преобразуем JSON обратно в список
            transactionsList.clear()
            transactionsList.addAll(loadedTransactions) // Добавляем загруженные транзакции в список
        }
    }

    private fun getCurrentDate(): String {
        // Возвращаем текущую дату в нужном формате
        return "19.10.2024" // Это пример, вы можете использовать свою реализацию
    }

    override fun onItemClick(transaction: Transaction) {
        // Обработка клика по элементу списка
    }

    private fun filterTransactions(type: String) {
        currentFilter = type
        val filteredList = transactionsList.filter { it.type == type }
        transactionsAdapter = TransactionAdapter(filteredList.toMutableList(), this) // Передаем MutableList
        binding.transactionsRecyclerView.adapter = transactionsAdapter
    }

    // Реализуем метод для удаления транзакции
    override fun onDeleteTransaction(transaction: Transaction) {
        // Удаляем транзакцию из основного списка
        val positionInMainList = transactionsList.indexOf(transaction)
        if (positionInMainList != -1) {
            transactionsList.removeAt(positionInMainList)
            saveTransactions() // Сохраняем изменения в SharedPreferences

            // Фильтруем заново после удаления
            currentFilter?.let {
                filterTransactions(it)
            } ?: transactionsAdapter.notifyDataSetChanged() // Обновляем весь список если нет фильтра
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
