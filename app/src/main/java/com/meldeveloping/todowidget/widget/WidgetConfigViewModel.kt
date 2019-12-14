package com.meldeveloping.todowidget.widget

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.repository.Repository

class WidgetConfigViewModel(
    private val repository: Repository
) : ViewModel() {

    private lateinit var adapter: MainListAdapter

    fun initListAdapter(id: Int? = null): MainListAdapter {
        adapter = if (id != null) {
            MainListAdapter(
                arrayListOf(repository.getItem(id)),
                clickable = false,
                longClickable = false
            )
        } else {
            MainListAdapter(repository.getAll(), longClickable = false)
        }
        return adapter
    }

    fun checkItem(id: Int) = repository.checkItem(id)

    fun getAdapter() = adapter
}