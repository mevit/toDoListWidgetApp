package com.meldeveloping.todowidget.model

import android.view.View
import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.repository.Repository

class WidgetConfigViewModel(
    private val repository: Repository
) : ViewModel() {

    private val adapter: MainListAdapter by lazy {
        MainListAdapter(repository.getAll())
    }

    fun getListAdapter() = adapter

    fun setAdapterListener(listenerFunction: () -> Unit ) {
        adapter.setClickListener(View.OnClickListener {
            listenerFunction()
        })
    }
}