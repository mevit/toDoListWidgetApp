package com.meldeveloping.todowidget.main

import android.app.Application
import com.meldeveloping.todowidget.koin.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ToDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ToDoApplication)
            modules(mainModule)
        }
    }
}