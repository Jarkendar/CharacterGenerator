package com.skrzypczak.charactergenerator

import android.net.Uri

interface CardPresenter {
    fun showImageInputChooser()
    fun createChooser(uriToObverse: Uri, mimeType: String)
}