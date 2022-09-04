package com.skrzypczak.charactergenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skrzypczak.charactergenerator.*
import com.skrzypczak.charactergenerator.database.CardModel
import com.skrzypczak.charactergenerator.databinding.FragmentCardSavesListBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardSavesFragment : Fragment(), CardSaveItemRecyclerViewAdapter.OnUserInteract {

    private var _binding: FragmentCardSavesListBinding? = null
    private val viewModel: CardSavesViewModel by viewModel()
    private val characterViewModel: CharacterViewModel by viewModel()

    private val cardSaver: CardSaver by inject()
    private val controller: CardsActivityController by inject()
    private val permissionHelper: PermissionHelper by inject()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: CardSaveItemRecyclerViewAdapter
    private lateinit var recyclerManager: RecyclerView.LayoutManager

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                initRecyclerView(binding.list)
            } else {
                activity?.finish()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardSavesListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        if (permissionHelper.hasReadMediaPermission()) {
            initRecyclerView(binding.list)
        } else {
            permissionHelper.startReadMediaPermissionFlow(requireActivity(), requestPermissionLauncher)
        }

        return binding.root
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.list)
        recyclerAdapter = CardSaveItemRecyclerViewAdapter(
            this,
            viewModel.cardsList.value?.toMutableList() ?: emptyList(),
            cardSaver
        )
        recyclerManager = LinearLayoutManager(context)
        with(recyclerView) {
            layoutManager = recyclerManager
            adapter = recyclerAdapter
        }

        viewModel.cardsList.observe(viewLifecycleOwner) {
            recyclerAdapter.updateData(it)
        }
    }

    override fun onItemClick(cardModel: CardModel) {
        characterViewModel.loadCard(cardModel)
    }

    override fun onItemLongClick(view: View, cardModel: CardModel) {
        PopupMenu(view.context, view).apply {
            setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.remove -> {
                        controller.removeCard(cardModel)
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.card_list_pop_menu)
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}