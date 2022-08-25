package com.skrzypczak.charactergenerator

import android.app.Application
import com.skrzypczak.charactergenerator.di.appModule
import com.skrzypczak.charactergenerator.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CGApplication: Application() {


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CGApplication)
            modules(appModule, databaseModule)
        }
    }
}