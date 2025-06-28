package com.mdrlzy.budgetwise.presentation

import android.app.Application
import com.mdrlzy.budgetwise.app.BuildConfig
import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import com.mdrlzy.budgetwise.di.DaggerAppComponent

class App : Application(), CoreComponentProvider {
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
}
