package com.meldeveloping.todowidget.koin

import com.meldeveloping.todowidget.model.EditViewModel
import com.meldeveloping.todowidget.model.MainViewModel
import com.meldeveloping.todowidget.repository.edit.EditRepository
import com.meldeveloping.todowidget.repository.edit.EditRepositoryImpl
import com.meldeveloping.todowidget.repository.main.MainRepository
import com.meldeveloping.todowidget.repository.main.MainRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModelModule = module {
    single<MainRepository> { MainRepositoryImpl() }

    viewModel { MainViewModel(get()) }
}

val editViewModelModule = module {
    single<EditRepository> { EditRepositoryImpl() }

    viewModel { EditViewModel(get()) }
}

val mainModule = listOf(mainViewModelModule, editViewModelModule)