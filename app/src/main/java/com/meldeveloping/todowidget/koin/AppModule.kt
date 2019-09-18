package com.meldeveloping.todowidget.koin

import com.meldeveloping.todowidget.model.EditViewModel
import com.meldeveloping.todowidget.model.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModelModule = module {
    viewModel { MainViewModel() }
}

val editViewModelModule = module {
    viewModel { EditViewModel() }
}

val mainModule = listOf(mainViewModelModule, editViewModelModule)