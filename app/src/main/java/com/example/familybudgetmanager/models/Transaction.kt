package com.example.familybudgetmanager.models

data class Transaction(
    val title: String,
    val category: String,
    val amount: String,
    val date: String,
    val type: String
)