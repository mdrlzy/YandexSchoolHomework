package com.mdrlzy.budgetwise.di

import android.app.Application
import android.content.Context
import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.core.di.module.DBModule
import com.mdrlzy.budgetwise.core.di.module.NetworkModule
import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DBModule::class])
interface AppComponent : CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context,
            @BindsInstance buildConfigFields: BuildConfigFields,
        ): AppComponent
    }
}
