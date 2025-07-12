package com.mdrlzy.budgetwise.feature.categories.impl.di

import android.content.Context
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider

object CategoriesComponentHolder {
    private var component: CategoriesComponent? = null

    fun provide(ctx: Context): CategoriesComponent {
        component ?: let {
            val app = ctx.applicationContext
            val coreComponent = (app as CoreComponentProvider).provideCoreComponent()
            component =
                DaggerCategoriesComponent.builder().coreComponent(coreComponent).build()
        }
        return component!!
    }
}