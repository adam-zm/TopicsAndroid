package com.example.topics

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TopicsApplication: Application() {
    override fun onCreate(){
        super.onCreate()
        startKoin {
            modules(appModule)
            androidLogger()
            androidContext(this@TopicsApplication)
        }
    }
}