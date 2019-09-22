package com.meldeveloping.todowidget.model

import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.repository.Repository

class MainViewModel(
    private val repository: Repository
) {

    fun getAdapterForMainList(): MainListAdapter {
        return MainListAdapter(repository.getAll())
    }
}