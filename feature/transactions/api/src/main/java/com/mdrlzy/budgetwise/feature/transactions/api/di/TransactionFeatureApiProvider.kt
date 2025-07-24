package com.mdrlzy.budgetwise.feature.transactions.api.di

interface TransactionFeatureApiProvider {
    fun provideTransactionFeatureApi(): TransactionsFeatureApi
}