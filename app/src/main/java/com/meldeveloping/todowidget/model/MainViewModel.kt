package com.meldeveloping.todowidget.model

import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.repository.Repository

class MainViewModel(
    val repository: Repository
) {

    fun getAdapterForMainList(): MainListAdapter {
        repository.save()
        return MainListAdapter(repository.getAll())
    }
}