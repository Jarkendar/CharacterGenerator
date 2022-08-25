package com.skrzypczak.charactergenerator.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsDao {

    @Query("SELECT * FROM cards ORDER BY timeStamp ASC")
    fun getAll(): Flow<List<CardModel>>

    @Query("SELECT * FROM cards WHERE timeStamp = :timestamp LIMIT 1")
    fun get(timestamp: Long): CardModel

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cardModel: CardModel)

    @Delete
    suspend fun delete(cardModel: CardModel)

    @Query("DELETE FROM cards")
    suspend fun deleteAll()
}