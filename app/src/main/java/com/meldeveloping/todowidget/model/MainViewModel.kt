package com.meldeveloping.todowidget.model

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.repository.Repository

class MainViewModel(
    private val repository: Repository
) : ViewModel() {

    fun getAdapterForMainList(): MainListAdapter {
        return MainListAdapter(repository.getAll())
    }
}