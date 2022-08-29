package com.skrzypczak.charactergenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skrzypczak.charactergenerator.CardSaver
import com.skrzypczak.charactergenerator.CardsActivityController
import com.skrzypczak.charactergenerator.CharacterViewModel
import com.skrzypczak.charactergenerator.R
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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: CardSaveItemRecyclerViewAdapter
    private lateinit var recyclerManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardSavesListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView(binding.list)

        viewModel.cardsList.observe(viewLifecycleOwner) {
            recyclerAdapter.updateData(it)
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