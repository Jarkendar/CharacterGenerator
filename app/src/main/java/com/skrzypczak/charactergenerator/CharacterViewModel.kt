package com.skrzypczak.charactergenerator

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class CharacterViewModel(private val context: Context): ViewModel() {

    private val _characterName = MutableLiveData<String>().apply { postValue("") }
    val characterName: LiveData<String> = _characterName

    private val _race = MutableLiveData<String>().apply { postValue("") }
    val race: LiveData<String> = _race

    private val _attrStrength = MutableLiveData<Int>().apply { postValue(3) }
    val attrStrength: LiveData<Int> = _attrStrength

    private val _attrWisdom = MutableLiveData<Int>().apply { postValue(3) }
    val attrWisdom: LiveData<Int> = _attrWisdom

    private val _attrAgility = MutableLiveData<Int>().apply { postValue(3) }
    val attrAgility: LiveData<Int> = _attrAgility

    private val _attrSpirit = MutableLiveData<Int>().apply { postValue(3) }
    val attrSpirit: LiveData<Int> = _attrSpirit

    private val _attrWit = MutableLiveData<Int>().apply { postValue(2) }
    val attrWit: LiveData<Int> = _attrWit

    private val _inspirationLimit = MutableLiveData<Int>().apply { postValue(4) }
    val inspirationLimit: LiveData<Int> = _inspirationLimit

    private val _damageLimit = MutableLiveData<Int>().apply { postValue(5) }
    val damageLimit: LiveData<Int> = _damageLimit

    private val _fearLimit = MutableLiveData<Int>().apply { postValue(5) }
    val fearLimit: LiveData<Int> = _fearLimit

    private val _passiveSkill = MutableLiveData<String>().apply { postValue("") }
    val passiveSkill: LiveData<String> = _passiveSkill

    private val _history = MutableLiveData<String>().apply { postValue("") }
    val history: LiveData<String> = _history

    private val _role = MutableLiveData<String>().apply { postValue("") }
    val role: LiveData<String> = _role

    private val _suggestItems = MutableLiveData<String>().apply { postValue("") }
    val suggestItems: LiveData<String> = _suggestItems

    private var obverseLayout: ConstraintLayout? = null
    private var reverseLayout: ConstraintLayout? = null
    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val TAG = "*****"

    init {
        Log.d(TAG, ": $this")
    }

    fun setCharacterName(name: String) {
        _characterName.value = name
    }

    fun setRace(race: String) {
        _race.value = race
    }

    fun setStrength(value: Int) {
        _attrStrength.value = value
    }

    fun setWisdom(value: Int) {
        _attrWisdom.value = value
    }

    fun setAgility(value: Int) {
        _attrAgility.value = value
    }

    fun setSpirit(value: Int) {
        _attrSpirit.value = value
    }

    fun setWit(value: Int) {
        _attrWit.value = value
    }

    fun setInspirationLimit(value: Int) {
        _inspirationLimit.value = value
    }

    fun setDamageLimit(value: Int) {
        _damageLimit.value = value
    }

    fun setFearLimit(value: Int) {
        _fearLimit.value = value
    }

    fun setPassiveSkill(text: String) {
        _passiveSkill.value = text
    }

    fun setHistory(text: String) {
        _history.value = text
    }

    fun setSuggestRole(role: String) {
        _role.value = role
    }

    fun setSuggestItems(text: String) {
        _suggestItems.value = text
    }

    fun setObverseLayout(view: ConstraintLayout) {
        Log.d(TAG, ":2 $this; $view")
        obverseLayout = view
    }

    fun setReverseLayout(view: ConstraintLayout) {
        reverseLayout = view
    }

    fun chooseImage() {
        Log.d(TAG, ":4 $this")
        MaterialAlertDialogBuilder(obverseLayout!!.context)
            .setTitle("Wybierz źródło")
            .setItems(arrayOf("Zrób zdjęcie", "Wybierz zdjęcie")) { dialog, which ->
                when(which) {
                    0 -> Log.d("*****", "0")
                    1 -> Log.d("*****", "1")
                    else -> Log.d("*****", "-1")
                }

            }
            .show()
    }

    fun generateCard() {
        Log.d(TAG, ":3 $this;")
        val layout = obverseLayout ?: return
        ioScope.launch {
            try {
                val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                layout.draw(canvas)

                val uri = saveBitmap(context, bitmap, Bitmap.CompressFormat.PNG, "image/png", "test.png")

                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_EMAIL, "arageros96@gmail.com")
                    putExtra(Intent.EXTRA_SUBJECT, "On The Job")
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "image/png"
                }
                mainScope.launch {
                    context.startActivity(Intent.createChooser(intent, "Share you on the jobing"))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    fun saveBitmap(
        context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
        mimeType: String, displayName: String
    ): Uri {

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }

        val resolver = context.contentResolver
        var uri: Uri? = null

        try {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException("Failed to create new MediaStore record.")

            resolver.openOutputStream(uri)?.use {
                if (!bitmap.compress(format, 95, it))
                    throw IOException("Failed to save bitmap.")
            } ?: throw IOException("Failed to open output stream.")

            return uri

        } catch (e: IOException) {

            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(orphanUri, null, null)
            }

            throw e
        }
    }
}