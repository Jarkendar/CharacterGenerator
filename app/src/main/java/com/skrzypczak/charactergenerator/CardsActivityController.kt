package com.skrzypczak.charactergenerator

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import com.skrzypczak.charactergenerator.database.CardModel
import com.skrzypczak.charactergenerator.database.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class CardsActivityController(private val repository: Repository, private val cardSaver: CardSaver) {

    private lateinit var presenter: CardPresenter

    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    fun initialize(presenter: CardPresenter) {
        this.presenter = presenter
    }

    fun getAllSavedCard() = repository.allCards()

    fun readImage(uri: Uri?): Drawable? {
        return cardSaver.getDrawableFromUri(uri)
    }

    fun insertCard(cardModel: CardModel, image: Drawable?) {
        ioScope.launch {
            val bitmap = (image as? BitmapDrawable)?.bitmap
            val uri = cardSaver.saveBitmap(bitmap, Bitmap.CompressFormat.PNG, "image/png", UUID.randomUUID().toString()).await()
            repository.insertCard(cardModel.apply { imageUri = uri ?: Uri.EMPTY })
        }
    }

    fun removeCard(cardModel: CardModel) {
        ioScope.launch {
            cardSaver.removeImage(cardModel.imageUri)
            repository.deleteCard(cardModel)
        }
    }

    fun chooseImage() {
        presenter.showImageInputChooser()
    }

    fun generateCard(obverseBitmap: Bitmap?, reverseBitmap: Bitmap?) {
        obverseBitmap ?: return
        reverseBitmap ?: return
        //todo feature better manage empty data

        ioScope.launch {
            try {
                val mimeType = "image/png"
                val deferredToObverse = cardSaver.saveBitmap(obverseBitmap, Bitmap.CompressFormat.PNG, mimeType, "obverse.png")
                val deferredToReverse = cardSaver.saveBitmap(reverseBitmap, Bitmap.CompressFormat.PNG, mimeType, "reverse.png")

                val uriToObverse = deferredToObverse.await()
                val uriToReverse = deferredToReverse.await()

                mainScope.launch {
                    if (uriToObverse == null || uriToReverse == null) {
//                        presenter.showError()
                    } else {
                        presenter.createChooser(uriToObverse, uriToReverse, mimeType)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                mainScope.launch {
//                    presenter.showError(e.message)
                }
            }
        }
    }
}