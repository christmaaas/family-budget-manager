package com.example.familybudgetmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.familybudgetmanager.databinding.FragmentTransactionDetailsBinding

class TransactionDetails : Fragment() {
    private var _binding: FragmentTransactionDetailsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentTransactionDetailsBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = TransactionDetailsArgs.fromBundle(requireArguments())
        binding.titleEditText.text = args.title
        binding.categoryEditText.text = args.category
        binding.amountEditText.text = args.amount
        binding.descriptionEditText.text = args.description
        binding.transactionDateText.text = args.date
        binding.transactionUserText.text = args.username

        binding.okTransactionButton.setOnClickListener {
            val action = TransactionDetailsDirections.actionTransactionDetailsFragmentToHistoryFragment(
                "",
                "",
                "",
                "",
                ""
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
