package com.skrzypczak.charactergenerator

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skrzypczak.charactergenerator.databinding.ActivityCardBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardsActivity : FragmentActivity(), CardPresenter {

    private lateinit var binding: ActivityCardBinding
    private lateinit var viewPager: ViewPager2

    private val controller: CardsActivityController by inject()
    private val viewModel: CharacterViewModel by viewModel()

    private val openImageChooserForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@registerForActivityResult
            val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, uri)

            viewModel.setCharacterImage(ImageDecoder.decodeDrawable(source))
        }
    }

    private val captureCameraForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap: Bitmap = result.data?.extras?.get("data") as Bitmap
            viewModel.setCharacterImage(BitmapDrawable(resources, bitmap))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).hide(WindowInsetsCompat.Type.statusBars())

        viewPager = findViewById(R.id.pager)
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when(position) {
                    0 -> findViewById<FloatingActionButton>(R.id.random_stats_fab).show()
                    1 -> findViewById<FloatingActionButton>(R.id.random_stats_fab).hide()
                }
                super.onPageSelected(position)
            }
        })

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = CardActivityPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            viewModel.generateCard()
        }

        controller.initialize(this)
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun showImageInputChooser() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.input_chooser_title)
            .setItems(R.array.chooser_input_method) { _, which ->
                when (which) {
                    0 -> captureCameraImage()
                    1 -> openImagePicker()
                    else -> Log.d("*****", "-1")
                }
            }
            .show()
    }

    private fun openImagePicker() {
        openImageChooserForResult.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

    private fun captureCameraImage() {
        captureCameraForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    override fun createChooser(uriToObverse: Uri, uriToReverse: Uri, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_subject))
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(uriToObverse, uriToReverse))
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