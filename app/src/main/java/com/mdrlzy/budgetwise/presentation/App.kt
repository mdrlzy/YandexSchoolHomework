package com.mdrlzy.budgetwise.presentation

import android.app.Application
import com.mdrlzy.budgetwise.app.BuildConfig
import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import com.mdrlzy.budgetwise.di.DaggerAppComponent
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.categories.api.di.CategoriesFeatureApi
import com.mdrlzy.budgetwise.feature.categories.api.di.CategoriesFeatureApiProvider
import com.mdrlzy.budgetwise.feature.categories.impl.di.CategoriesComponentHolder

class App : Application(), CoreComponentProvider, CategoriesFeatureApiProvider {
    lateinit var component: CoreComponent
        private set

    override fun onCreate() {
        super.onCreate()
        val buildConfigFields = BuildConfigFields(BuildConfig.BEARER_TOKEN)
        component =
            DaggerAppComponent
                .factory()
                .create(this, this.applicationContext, buildConfigFields)
    }

    override fun provideCoreComponent() = component

    override fun provideCategoriesFeatureApi() = CategoriesComponentHolder.provide(this)


}
