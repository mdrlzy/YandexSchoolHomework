package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mdrlzy.budgetwise.feature.transactions.impl.di.TransactionsComponentHolder

class SyncTransactionsWorker(
    val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        Log.i(NAME, "Worker started")

        val result = TransactionsComponentHolder
            .provide(context)
            .syncTransactionsUseCase()
            .invoke()

        return result.fold(
            ifLeft = {
                Log.e(NAME, "Sync failed: ${it.message}")
                Result.failure()
            },
            ifRight = {
                Log.i(NAME, "Sync completed successfully")
                Result.success()
            },
        )
    }

    companion object {
        const val NAME = "SyncTransactionsWorker"
    }
}