package com.skrzypczak.charactergenerator.di

import com.skrzypczak.charactergenerator.CardsActivityController
import com.skrzypczak.charactergenerator.CharacterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        CardsActivityController(androidContext())
    }

    single {
        CharacterViewModel(get())
    }
}