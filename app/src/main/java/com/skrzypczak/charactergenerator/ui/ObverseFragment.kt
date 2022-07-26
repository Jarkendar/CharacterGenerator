package com.skrzypczak.charactergenerator.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.skrzypczak.charactergenerator.CharacterViewModel
import com.skrzypczak.charactergenerator.R
import com.skrzypczak.charactergenerator.createViewBitmap
import com.skrzypczak.charactergenerator.databinding.FragmentObverseBinding
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
        binding.viewModel = viewModel
        val root: View = binding.root

        root.findViewById<EditText>(R.id.edit_character_name).addTextChangedListener {
            viewModel.setCharacterName(it.toString())
        }

        root.findViewById<Spinner>(R.id.spinner_races).onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
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
        viewModel.initObverseListener(this)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getScreenShot(): Bitmap {//todo prepare layout to make beautiful screenshot
        return binding.root.findViewById<ConstraintLayout>(R.id.obverse_card_layout).createViewBitmap()
    }
}