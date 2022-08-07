package com.skrzypczak.charactergenerator.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.skrzypczak.charactergenerator.CharacterViewModel
import com.skrzypczak.charactergenerator.R
import com.skrzypczak.charactergenerator.createViewBitmap
import com.skrzypczak.charactergenerator.databinding.FragmentReverseBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReverseFragment : Fragment(), PageListener {

    private var _binding: FragmentReverseBinding? = null

    private val viewModel: CharacterViewModel by viewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReverseBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        val root: View = binding.root

        viewModel.initReverseListener(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //todo to change
    override fun getScreenShot(): Bitmap {
        return binding.root.findViewById<ConstraintLayout>(R.id.reverse_card_layout).createViewBitmap()
    }
}