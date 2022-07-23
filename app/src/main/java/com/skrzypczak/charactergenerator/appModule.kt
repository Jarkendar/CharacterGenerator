package com.skrzypczak.charactergenerator

import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        CharacterViewModel(androidContext())
    }
}