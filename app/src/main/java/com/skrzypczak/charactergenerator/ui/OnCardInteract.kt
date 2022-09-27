package com.skrzypczak.charactergenerator.ui

import com.skrzypczak.charactergenerator.database.CardModel

interface OnCardInteract {
    fun onItemClick(cardModel: CardModel)
    fun onRemoveClick(cardModel: CardModel)
}