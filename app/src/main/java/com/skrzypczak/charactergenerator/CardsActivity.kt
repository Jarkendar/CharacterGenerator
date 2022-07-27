package com.skrzypczak.charactergenerator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skrzypczak.charactergenerator.databinding.ActivityCardBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardsActivity : AppCompatActivity(), CardPresenter {

    private lateinit var binding: ActivityCardBinding

    private val controller: CardsActivityController by inject()
    private val viewModel: CharacterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).hide(WindowInsetsCompat.Type.statusBars())
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_card)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_obverse, R.id.navigation_reverse
            )
        )

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            viewModel.generateCard()
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.visibility = View.GONE

        controller.initialize(this)
    }

    override fun showImageInputChooser() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.input_chooser_title)
            .setItems(R.array.chooser_input_method) { _, which ->
                when (which) {
                    0 -> Log.d("*****", "0")
                    1 -> Log.d("*****", "1")
                    else -> Log.d("*****", "-1")
                }
            }
            .show()
    }

    override fun createChooser(uriToObverse: Uri, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_subject))
            putExtra(Intent.EXTRA_STREAM, uriToObverse)
            type = mimeType
        }

        this.startActivity(
            Intent.createChooser(
                intent,
                getString(R.string.intent_output_chooser_title)
            )
        )
    }
}