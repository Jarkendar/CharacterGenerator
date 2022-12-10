package com.skrzypczak.charactergenerator.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.skrzypczak.charactergenerator.CharacterViewModel
import com.skrzypczak.charactergenerator.R
import com.skrzypczak.charactergenerator.databinding.FragmentObverseBinding
import com.skrzypczak.charactergenerator.utils.createViewBitmap
import com.skrzypczak.charactergenerator.utils.hide
import com.skrzypczak.charactergenerator.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class ObverseFragment : Fragment(), PageListener {

    private var _binding: FragmentObverseBinding? = null

    private val viewModel: CharacterViewModel by viewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObverseBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val root: View = binding.root

        val racesSpinner = root.findViewById<Spinner>(R.id.spinner_races)
        racesSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setRace(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ignore
            }
        }
        viewModel.race.observe(viewLifecycleOwner) { currentRace ->
            var currentRaceIndex = 0
            resources.getStringArray(R.array.races).forEachIndexed { index, race ->
                if (race == currentRace) {
                    currentRaceIndex = index
                }
            }

            racesSpinner.setSelection(currentRaceIndex)
        }
        viewModel.initObverseListener(this)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getScreenShot(): Bitmap {
        prepareLayoutViewsVisibility()
        val bitmap = binding.root.findViewById<ConstraintLayout>(R.id.obverse_card_layout).createViewBitmap()
        resetLayoutViewsVisibility()
        return bitmap
    }

    private fun prepareLayoutViewsVisibility() {
        binding.root.findViewById<MaterialButton>(R.id.image_chooser_button).hide()
    }

    private fun resetLayoutViewsVisibility() {
        binding.root.findViewById<MaterialButton>(R.id.image_chooser_button).show()
    }

}