package com.mdrlzy.budgetwise.presentation

import android.app.Application
import com.mdrlzy.budgetwise.di.AppComponent
import com.mdrlzy.budgetwise.di.DaggerAppComponent

class App : Application() {
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.factory().create(this, this.applicationContext)
    }
}