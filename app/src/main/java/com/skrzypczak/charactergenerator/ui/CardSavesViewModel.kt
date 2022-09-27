package com.skrzypczak.charactergenerator.ui

import androidx.lifecycle.ViewModel
import com.skrzypczak.charactergenerator.CardsActivityController
import com.skrzypczak.charactergenerator.database.CardModel
import kotlinx.coroutines.flow.Flow

class CardSavesViewModel(controller: CardsActivityController): ViewModel() {

    val cardsList: Flow<List<CardModel>> = controller.getAllSavedCard()

}