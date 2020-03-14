package com.ben.autodocchat.core

import android.app.Application
import com.ben.datamodule.dataModule
import com.ben.exhibitionapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    viewModelModule
                )
            )
        }
    }
}