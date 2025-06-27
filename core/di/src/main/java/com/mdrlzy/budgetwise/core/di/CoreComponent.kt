package com.mdrlzy.budgetwise.core.di

import android.app.Application
import android.content.Context
import com.mdrlzy.budgetwise.core.di.module.NetworkModule
import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
    ],
)

interface CoreComponent {
    fun networkStatus(): NetworkStatus
    fun buildConfigFields(): BuildConfigFields

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context,
            @BindsInstance buildConfigFields: BuildConfigFields,
        ): CoreComponent
    }
}