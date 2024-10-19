package com.example.familybudgetmanager.fragments

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

class History : Fragment(), TransactionAdapter.RecyclerViewEvent {
    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentHistoryBinding is null")

    private val args: HistoryArgs by navArgs()

    private lateinit var transactionsAdapter: TransactionAdapter
    private val transactionsList = mutableListOf<Transaction>() // Изменено на mutableList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем данные переданные из AddTransactionFragment

        transactionsAdapter = TransactionAdapter(transactionsList, this)
        binding.transactionsRecyclerView.adapter = transactionsAdapter
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(context)

        receiveTransactionData()

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

    private fun receiveTransactionData() {
        var title: String
        var category: String
        var amount: Double
        var description: String
        var transactionType: String

        try {
            title = args.title
            category = args.category
            amount = args.amount.toDouble()
            description = args.description
            transactionType = args.transactionType
        } catch (e: Exception) {
            title = ""
            category = ""
            amount = 0.0
            description = ""
            transactionType = ""
            e.printStackTrace()
        }
        if (title.isNotEmpty() && category.isNotEmpty() && amount != 0.0 && description.isNotEmpty() && transactionType.isNotEmpty()) {
            val transaction = Transaction(title, category, amount, getCurrentDate(), transactionType)
            transactionsList.add(transaction)
            transactionsAdapter.notifyItemInserted(transactionsList.size - 1) // Уведомляем адаптер о добавлении нового элемента
        }
    }

    private fun getCurrentDate(): String {
        // Возвращаем текущую дату в нужном формате
        // Можно использовать SimpleDateFormat для форматирования даты
        return "19.10.2024" // Это пример, вы можете использовать свою реализацию
    }

    override fun onItemClick(transaction: Transaction) {
        // Обработка клика по элементу списка
    }

    private fun filterTransactions(type: String) {
        // Логика для фильтрации транзакций по типу
        val filteredList = transactionsList.filter { it.type == type }
        transactionsAdapter = TransactionAdapter(filteredList, this)
        binding.transactionsRecyclerView.adapter = transactionsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
