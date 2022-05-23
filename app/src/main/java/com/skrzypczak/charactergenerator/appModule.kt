package com.skrzypczak.charactergenerator

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        CharacterViewModel()
    }

}