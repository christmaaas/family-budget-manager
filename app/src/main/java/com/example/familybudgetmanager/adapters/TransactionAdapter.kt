package com.example.familybudgetmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familybudgetmanager.databinding.TransactionItemBinding
import com.example.familybudgetmanager.models.Transaction

class TransactionAdapter(
    private val transactionList: List<Transaction>,
    private val listener: RecyclerViewEvent
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(transaction: Transaction) {
            binding.title.text = transaction.title
            binding.category.text = transaction.category
            binding.money.text = transaction.amount.toString()
            binding.date.text = transaction.date
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(transactionList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int = transactionList.size

    interface RecyclerViewEvent {
        fun onItemClick(transaction: Transaction)
    }
}
