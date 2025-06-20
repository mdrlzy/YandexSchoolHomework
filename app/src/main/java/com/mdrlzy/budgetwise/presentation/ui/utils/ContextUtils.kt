package com.mdrlzy.budgetwise.presentation.ui.utils

import android.content.Context
import com.mdrlzy.budgetwise.di.AppComponent
import com.mdrlzy.budgetwise.presentation.App

val Context.appComponent: AppComponent
    get() = (applicationContext as App).component