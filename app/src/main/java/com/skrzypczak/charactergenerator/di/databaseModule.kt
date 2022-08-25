package com.skrzypczak.charactergenerator.di

import androidx.room.Room
import com.skrzypczak.charactergenerator.database.CardsRoomDatabase
import com.skrzypczak.charactergenerator.database.Repository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single { Room.databaseBuilder(androidApplication(), CardsRoomDatabase::class.java, "cardsRoomDatabase").build() }

    single {
        Repository(get())
    }
}