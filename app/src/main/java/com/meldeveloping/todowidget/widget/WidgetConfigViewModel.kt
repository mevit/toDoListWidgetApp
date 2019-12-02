package com.meldeveloping.todowidget.widget

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.repository.Repository

class WidgetConfigViewModel(
    private val repository: Repository
) : ViewModel() {

    private lateinit var adapter: MainListAdapter

    fun getListAdapter(): MainListAdapter {
        adapter = MainListAdapter(repository.getAll())
        return adapter
    }

    fun getItemDate(id: Int) = repository.getItem(id).toDoListDate

    fun getItemTitle(id: Int) = repository.getItem(id).toDoListTitle

    fun getAdapter() = adapter
}