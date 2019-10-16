package com.meldeveloping.todowidget.model

import android.view.View
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

    fun setAdapterListener(listenerFunction: () -> Unit ) {
        adapter.setClickListener(View.OnClickListener {
            listenerFunction()
        })
    }

    fun getToDoListTitle(id: Int) = repository.getItem(id).toDoListTitle

}