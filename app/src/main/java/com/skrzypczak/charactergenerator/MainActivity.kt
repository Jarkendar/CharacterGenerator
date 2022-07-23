package com.skrzypczak.charactergenerator

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.skrzypczak.charactergenerator.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val model: CharacterViewModel by viewModel()

    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.viewModel = model

        findViewById<EditText>(R.id.edit_character_name).addTextChangedListener {
            model.setCharacterName(it.toString())
        }

        findViewById<Spinner>(R.id.spinner_race).onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                model.setRace(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ignore
            }
        }

        findViewById<SeekBar>(R.id.seekBar_strong).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setStrength(it) })
        findViewById<SeekBar>(R.id.seekBar_wisdom).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setWisdom(it) })
        findViewById<SeekBar>(R.id.seekBar_agility).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setAgility(it) })
        findViewById<SeekBar>(R.id.seekBar_spirit).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setSpirit(it) })
        findViewById<SeekBar>(R.id.seekBar_wit).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setWit(it) })
        findViewById<SeekBar>(R.id.seekBar_inspiration).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setInspirationLimit(it) })
        findViewById<SeekBar>(R.id.seekBar_damage).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setDamageLimit(it) })
        findViewById<SeekBar>(R.id.seekBar_fear).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setFearLimit(it) })

        findViewById<EditText>(R.id.edit_passive_skill).addTextChangedListener {
            model.setPassiveSkill(it.toString())
        }

        findViewById<EditText>(R.id.edit_history).addTextChangedListener {
            model.setHistory(it.toString())
        }

        findViewById<Spinner>(R.id.spinner_suggest_role).onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                model.setSuggestRole(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ignore
            }
        }

        findViewById<EditText>(R.id.edit_suggest_items).addTextChangedListener {
            model.setSuggestItems(it.toString())
        }

        findViewById<Button>(R.id.button_start_activity).setOnClickListener {
            startActivity(Intent(this, CardsActivity::class.java))
        }

        findViewById<Button>(R.id.button_done).setOnClickListener { _ ->
            val layout = findViewById<NestedScrollView>(R.id.main_layout)
            val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            layout.draw(canvas)
            ioScope.launch {
                try {
                    val uri = saveBitmap(this@MainActivity.applicationContext, bitmap, Bitmap.CompressFormat.PNG, "image/png", "test.png")

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_EMAIL, "arageros96@gmail.com")
                        putExtra(Intent.EXTRA_SUBJECT, "On The Job")
                        putExtra(Intent.EXTRA_STREAM, uri)
                        type = "image/png"
                    }
                    mainScope.launch {
                        startActivity(Intent.createChooser(intent, "Share you on the jobing"))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
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

            e.printStackTrace()
            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(orphanUri, null, null)
            }

            throw e
        }
    }

    private fun createSeekBarChangeListener(setAttr: (value: Int) -> Unit) = object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setAttr(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //ignore
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //ignore
            }
        }


}