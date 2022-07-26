package com.skrzypczak.charactergenerator

import android.content.Intent

interface CardPresenter {
    fun showImageInputChooser()
    fun createChooser(intent: Intent)
}