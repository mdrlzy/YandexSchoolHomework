package com.mdrlzy.budgetwise.feature.transactions.impl.di

import android.content.Context
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.feature.categories.api.di.CategoriesFeatureApiProvider

object TransactionsComponentHolder {
    private var component: TransactionsComponent? = null

    fun provide(ctx: Context): TransactionsComponent {
        component ?: let {
            val app = ctx.applicationContext
            val coreComponent = (app as CoreComponentProvider).provideCoreComponent()
            val categoriesFeatureApi =
                (app as CategoriesFeatureApiProvider).provideCategoriesFeatureApi()
            component =
                DaggerTransactionsComponent.builder().coreComponent(coreComponent)
                    .categoriesFeatureApi(categoriesFeatureApi).build()
        }
        return component!!
    }
}
