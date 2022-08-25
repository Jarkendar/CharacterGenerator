package com.skrzypczak.charactergenerator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CardModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CardsRoomDatabase: RoomDatabase() {

    abstract fun cardsDao(): CardsDao
}