package com.example.familybudgetmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.familybudgetmanager.R
import com.example.familybudgetmanager.databinding.BudgetItemBinding
import com.example.familybudgetmanager.models.Budget

class BudgetAdapter(
    private val budgetList: MutableList<Budget>,
    private val listener: RecyclerViewEvent
) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    inner class BudgetViewHolder(private val binding: BudgetItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener {
                showPopupMenu(it, adapterPosition)
                true
            }
        }

        fun bind(budget: Budget) {
            binding.budgetAmount.text = budget.budgetAmount
            binding.budgetPeriodType.text = budget.budgetPeriodType
            binding.budgetDateTitle.text = budget.budgetDateTitle
            binding.userNameTitle.text = budget.userNameTitle
        }

        private fun showPopupMenu(view: View, position: Int) {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.context_menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_transaction -> {
                        listener.onDeleteBudget(budgetList[position])
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
                listener.onItemClick(budgetList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = BudgetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget = budgetList[position]
        holder.bind(budget)
    }

    override fun getItemCount(): Int = budgetList.size

    interface RecyclerViewEvent {
        fun onItemClick(budget: Budget)
        fun onDeleteBudget(budget: Budget)
    }
}
