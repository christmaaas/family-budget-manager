package com.example.familybudgetmanager.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.familybudgetmanager.databinding.FragmentSetBudgetBinding

class SetBudgetFragment : DialogFragment() {

    private var _binding: FragmentSetBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitBudgetButton.setOnClickListener {
            val budget = binding.budgetInput.text.toString().toFloatOrNull()
            val periodValue = binding.periodInput.text.toString().toIntOrNull()

            val selectedPeriodType = when (binding.periodRadioGroup.checkedRadioButtonId) {
                binding.daysRadioButton.id -> "Days"
                binding.weeksRadioButton.id -> "Weeks"
                binding.monthsRadioButton.id -> "Months"
                else -> null
            }

            if (budget == null || periodValue == null || selectedPeriodType == null) {
                Toast.makeText(context, "Please fill all fields and select a period type", Toast.LENGTH_SHORT).show()
            } else {
                val action = SetBudgetFragmentDirections.actionSetBudgetFragmentToHomeFragment(
                    budget.toString(),
                    periodValue.toString(),
                    selectedPeriodType
                )
                findNavController().navigate(action)
            }
        }

        binding.cancelBudgetButton.setOnClickListener {
            dismiss()
            findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
