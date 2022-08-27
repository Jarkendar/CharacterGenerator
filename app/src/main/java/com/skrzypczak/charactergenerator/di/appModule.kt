package com.skrzypczak.charactergenerator.di

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.skrzypczak.charactergenerator.CardSaver
import com.skrzypczak.charactergenerator.CardsActivityController
import com.skrzypczak.charactergenerator.CharacterViewModel
import com.skrzypczak.charactergenerator.ui.CardSavesViewModel
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
        CardsActivityController(get(), get())
    }

    single {
        CharacterViewModel(get())
    }

    single {
        CardSavesViewModel(get())
    }

    single {
        Firebase.analytics
    }
}