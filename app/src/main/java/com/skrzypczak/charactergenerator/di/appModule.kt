package com.skrzypczak.charactergenerator.di

import com.skrzypczak.charactergenerator.CardSaver
import com.skrzypczak.charactergenerator.CardsActivityController
import com.skrzypczak.charactergenerator.CharacterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        CardSaver(androidContext(), CoroutineScope(Dispatchers.IO + Job()))
    }

    single {
        CardsActivityController(get())
    }

    single {
        CharacterViewModel(get())
    }
}