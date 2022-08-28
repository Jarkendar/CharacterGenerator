package com.skrzypczak.charactergenerator.database

import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "cards")
data class CardModel(
    @PrimaryKey(autoGenerate = false)
    val timeStamp: Date,
    val name: String,
    val race: String,
    @Embedded
    val attribution: CardAttribution,
    val passiveSkill: String,
    val history: String,
    val suggestStartSet: String,
    var imageUri: Uri? = Uri.EMPTY
    )

data class CardAttribution(
    val strength: Int,
    val wisdom: Int,
    val agility: Int,
    val spirit: Int,
    val wit: Int,
    val inspirationLimit: Int,
    val damageLimit: Int,
    val fearLimit: Int
)
