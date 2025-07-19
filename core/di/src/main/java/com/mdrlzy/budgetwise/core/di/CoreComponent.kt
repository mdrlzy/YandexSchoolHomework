package com.mdrlzy.budgetwise.core.di

import com.mdrlzy.budgetwise.core.db.BWDatabase
import com.mdrlzy.budgetwise.core.db.dao.AccountDao
import com.mdrlzy.budgetwise.core.db.dao.CategoryDao
import com.mdrlzy.budgetwise.core.db.dao.PendingTransactionDao
import com.mdrlzy.budgetwise.core.db.dao.TransactionDao
import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import com.mdrlzy.budgetwise.core.domain.Prefs
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import com.mdrlzy.budgetwise.core.network.BWApiClient
import dagger.Provides

interface CoreComponent {
    fun bwApiClient(): BWApiClient

    fun networkStatus(): NetworkStatus

    fun buildConfigFields(): BuildConfigFields

    fun accountRepo(): AccountRepo

    fun accountDao(): AccountDao

    fun categoryDao(): CategoryDao

    fun transactionDao(): TransactionDao

    fun pendingTransactionDao(): PendingTransactionDao

    fun prefs(): Prefs
}
