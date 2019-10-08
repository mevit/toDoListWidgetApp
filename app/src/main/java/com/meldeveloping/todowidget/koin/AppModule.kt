package com.meldeveloping.todowidget.koin

import androidx.room.Room
import com.meldeveloping.todowidget.db.room.ToDoListDao
import com.meldeveloping.todowidget.db.room.ToDoListDatabase
import com.meldeveloping.todowidget.model.EditViewModel
import com.meldeveloping.todowidget.model.MainViewModel
import com.meldeveloping.todowidget.repository.Repository
import com.meldeveloping.todowidget.repository.RoomDBRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { EditViewModel(get()) }
}

val repositoryModule = module {
    single { get<ToDoListDatabase>().toDoListDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            ToDoListDatabase::class.java,
            ToDoListDatabase.DB_NAME
        ).build()
    }

    single<Repository> { RoomDBRepository(get() as ToDoListDao) }
}

val mainModule = listOf(viewModelsModule, repositoryModule)