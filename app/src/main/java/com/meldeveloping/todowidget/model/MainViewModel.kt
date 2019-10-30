package com.meldeveloping.todowidget.model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.repository.Repository
import com.meldeveloping.todowidget.widget.WidgetProvider

class MainViewModel(
    private val repository: Repository,
    private val context: Context
) : ViewModel() {

    private lateinit var adapter: MainListAdapter
    private lateinit var list: ArrayList<ToDoList>

    fun getAdapterForMainList(): MainListAdapter {
        initMainListAdapter()
        return adapter
    }

    fun removeItem(position: Int){
        repository.delete(list[position])
        list.removeAt(position)
        adapter.notifyDataSetChanged()
        WidgetProvider.updateAppWidgets(context)
    }

    private fun initMainListAdapter(){
        list = repository.getAll()
        adapter = MainListAdapter(list)
    }
}