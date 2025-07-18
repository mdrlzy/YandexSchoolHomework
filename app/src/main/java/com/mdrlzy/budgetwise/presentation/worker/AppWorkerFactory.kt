package com.mdrlzy.budgetwise.presentation.worker

import androidx.work.DelegatingWorkerFactory
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.worker.SyncTransactionsWorkerFactory

class AppWorkerFactory : DelegatingWorkerFactory() {
    init {
        addFactory(
            SyncTransactionsWorkerFactory(),
        )
    }
}