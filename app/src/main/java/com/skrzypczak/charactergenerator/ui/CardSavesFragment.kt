package com.skrzypczak.charactergenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.skrzypczak.charactergenerator.CardsActivityController
import com.skrzypczak.charactergenerator.CharacterViewModel
import com.skrzypczak.charactergenerator.PermissionHelper
import com.skrzypczak.charactergenerator.database.CardModel
import com.skrzypczak.charactergenerator.ui.composable.CardSavesList
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardSavesFragment : Fragment(), OnCardInteract {

    private val viewModel: CardSavesViewModel by viewModel()
    private val characterViewModel: CharacterViewModel by viewModel()

    private val controller: CardsActivityController by inject()
    private val permissionHelper: PermissionHelper by inject()

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val storagePermissionIsGranted = mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            storagePermissionIsGranted.value = true
        } else {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!permissionHelper.hasReadMediaPermission()) {
            permissionHelper.startReadMediaPermissionFlow(
                requireActivity(),
                requestPermissionLauncher
            )
        } else {
            storagePermissionIsGranted.value = true
        }

        return ComposeView(requireContext()).apply {
            setContent {
                if (storagePermissionIsGranted.value) {
                    CardSavesList(viewModel, this@CardSavesFragment)
                } else {
                    Text(text = "Placeholder TMP")
                }
            }
        }
    }

    override fun onItemClick(cardModel: CardModel) {
        characterViewModel.loadCard(cardModel)
    }

    override fun onRemoveClick(cardModel: CardModel) {
        controller.removeCard(cardModel)
    }
}