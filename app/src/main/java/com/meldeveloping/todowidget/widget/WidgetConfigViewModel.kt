package com.meldeveloping.todowidget.widget

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.repository.Repository

class WidgetConfigViewModel(
    private val repository: Repository
) : ViewModel() {

    private lateinit var adapter: MainListAdapter

    fun initListAdapter(): MainListAdapter {
        adapter = MainListAdapter(repository.getAll(), longClickable = false)
        return adapter
    }

    fun getSelectedItemPosition(id: Int) = repository.getItem(id).toDoListPosition

    fun checkItem(id: Int) = repository.checkItem(id)

    fun getAdapter() = adapter
}