package com.skrzypczak.charactergenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.skrzypczak.charactergenerator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val model: CharacterViewModel by viewModels()

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

        findViewById<SeekBar>(R.id.seekBar_strong).setOnSeekBarChangeListener(createSeekBarChangeListener { model.setStrong(it) })
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