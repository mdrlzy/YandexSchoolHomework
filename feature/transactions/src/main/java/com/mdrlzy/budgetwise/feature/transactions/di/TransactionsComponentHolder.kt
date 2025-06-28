package com.mdrlzy.budgetwise.feature.transactions.di

import android.content.Context
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider

object TransactionsComponentHolder {
    private var component: TransactionsComponent? = null

    fun provide(ctx: Context): TransactionsComponent {
        component ?: let {
            val app = ctx.applicationContext
            val coreComponent = (app as CoreComponentProvider).provideCoreComponent()
            component =
                DaggerTransactionsComponent.builder().coreComponent(coreComponent).build()
        }
        return component!!
    }
}
