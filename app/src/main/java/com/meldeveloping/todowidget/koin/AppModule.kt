package com.meldeveloping.todowidget.koin

import androidx.room.Room
import com.meldeveloping.todowidget.db.room.ToDoListDao
import com.meldeveloping.todowidget.db.room.ToDoListDatabase
import com.meldeveloping.todowidget.model.EditViewModel
import com.meldeveloping.todowidget.model.MainViewModel
import com.meldeveloping.todowidget.repository.edit.EditRepository
import com.meldeveloping.todowidget.repository.edit.EditRepositoryImpl
import com.meldeveloping.todowidget.repository.main.MainRepository
import com.meldeveloping.todowidget.repository.main.MainRepositoryImpl
import org.koin.android.ext.koin.androidContext
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

val toDoListDaoModule = module {
    single<ToDoListDao> { get<ToDoListDatabase>().toDoListDao() }
    single { Room.databaseBuilder(androidContext(), ToDoListDatabase::class.java, ToDoListDatabase.DB_NAME) }
}

val mainModule = listOf(mainViewModelModule, editViewModelModule, toDoListDaoModule)