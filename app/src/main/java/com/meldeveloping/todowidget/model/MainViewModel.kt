package com.meldeveloping.todowidget.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.repository.Repository
import com.meldeveloping.todowidget.widget.ToDoListWidget

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

    fun removeItem(viewHolder: RecyclerView.ViewHolder){
        repository.delete(list[viewHolder.adapterPosition])
        list.removeAt(viewHolder.adapterPosition)
        adapter.notifyDataSetChanged()
        ToDoListWidget.refreshWidget(context)
    }

    private fun initMainListAdapter(){
        list = repository.getAll()
        adapter = MainListAdapter(list)
    }
}