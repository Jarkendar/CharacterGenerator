package com.skrzypczak.charactergenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.skrzypczak.charactergenerator.databinding.FragmentReverseBinding

class ReverseFragment : Fragment() {

    private var _binding: FragmentReverseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReverseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}