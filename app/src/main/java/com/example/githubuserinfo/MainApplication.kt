package com.example.githubuserinfo

import android.app.Application
import com.example.githubuserinfo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initDebug()

        // start koin
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    private fun initDebug() {
        Timber.plant(Timber.DebugTree())
    }

}