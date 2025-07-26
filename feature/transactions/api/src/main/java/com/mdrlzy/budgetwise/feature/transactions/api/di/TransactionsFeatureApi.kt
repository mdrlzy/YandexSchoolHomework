package com.mdrlzy.budgetwise.feature.transactions.api.di

import com.mdrlzy.budgetwise.feature.transactions.api.TransactionRepo

interface TransactionsFeatureApi {
    fun transactionRepo(): TransactionRepo
}