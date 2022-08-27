package com.skrzypczak.charactergenerator.database

class Repository(cardsRoomDatabase: CardsRoomDatabase) {

    private val cardsDao: CardsDao = cardsRoomDatabase.cardsDao()

    fun allCards() = cardsDao.getAll()

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