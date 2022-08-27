package com.skrzypczak.charactergenerator.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skrzypczak.charactergenerator.CardsActivityController
import com.skrzypczak.charactergenerator.database.CardModel
import kotlinx.coroutines.Dispatchers

class CardSavesViewModel(controller: CardsActivityController): ViewModel() {

    val cardsList: LiveData<List<CardModel>> = controller.getAllSavedCard().asLiveData(Dispatchers.IO)

}