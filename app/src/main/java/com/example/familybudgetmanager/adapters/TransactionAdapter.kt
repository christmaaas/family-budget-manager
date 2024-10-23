package com.example.familybudgetmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.familybudgetmanager.R
import com.example.familybudgetmanager.databinding.TransactionItemBinding
import com.example.familybudgetmanager.models.Transaction

class TransactionAdapter(
    private val transactionList: MutableList<Transaction>,
    private val listener: RecyclerViewEvent
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener {
                showPopupMenu(it, adapterPosition)
                true
            }
        }

        fun bind(transaction: Transaction) {
            binding.transactionUsernameTitle.text = transaction.username
            binding.transactionTitle.text = transaction.title
            binding.money.text = transaction.amount
            binding.date.text = transaction.date
        }

        private fun showPopupMenu(view: View, position: Int) {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.context_menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_transaction -> {
                        listener.onDeleteTransaction(transactionList[position])
                        true
                    }
                    else -> false
                }
            }
            popup.show()
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
        fun onDeleteTransaction(transaction: Transaction)
    }
}

