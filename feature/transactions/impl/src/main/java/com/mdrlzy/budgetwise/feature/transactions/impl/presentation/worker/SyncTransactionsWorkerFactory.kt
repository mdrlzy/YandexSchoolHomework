package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class SyncTransactionsWorkerFactory : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncTransactionsWorker::class.java.name ->
                SyncTransactionsWorker(
                    params = workerParameters,
                    context = appContext,
                )

            else -> null
        }
    }
}