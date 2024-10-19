package com.example.familybudgetmanager.fragments

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Устанавливаем слушатель клика для кнопки сохранения
        binding.saveTransactionButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val category = binding.categoryEditText.text.toString()
            val amount = binding.amountEditText.text.toString().toDoubleOrNull()
            val description = binding.descriptionEditText.text.toString()

            val selectedtransactionType = when (binding.transactionTypeRadioGroup.checkedRadioButtonId) {
                binding.spendingRadioButton.id -> "Spending"
                binding.profitRadioButton.id -> "Profit"
                else -> null
            }

            // Проверяем, что все поля заполнены
            if (title.isEmpty() || category.isEmpty() || amount == null || description.isEmpty() || selectedtransactionType == null) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val action = AddTransactionFragmentDirections.actionAddTransactionFragmentToHistoryFragment(
                    title,
                    category,
                    amount.toString(),
                    description,
                    selectedtransactionType
                )
                findNavController().navigate(action)
            }
        }

        binding.cancelTransactionButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
