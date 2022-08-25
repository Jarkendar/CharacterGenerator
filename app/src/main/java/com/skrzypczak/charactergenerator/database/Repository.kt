package com.skrzypczak.charactergenerator.database

import kotlinx.coroutines.flow.Flow

class Repository(cardsRoomDatabase: CardsRoomDatabase) {

    private val cardsDao: CardsDao = cardsRoomDatabase.cardsDao()

    val allCards: Flow<List<CardModel>> = cardsDao.getAll()

    fun get(timestamp: Long) = cardsDao.get(timestamp)

    suspend fun insertCard(cardModel: CardModel) {
        cardsDao.insert(cardModel)
    }

    suspend fun deleteCard(cardModel: CardModel) {
        cardsDao.delete(cardModel)
    }

    suspend fun deleteAll() {
        cardsDao.deleteAll()
    }

}