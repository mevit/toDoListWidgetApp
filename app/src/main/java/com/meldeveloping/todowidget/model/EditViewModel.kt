package com.meldeveloping.todowidget.model

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.EditReadAdapter
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.repository.Repository

class EditViewModel(
    private val repository: Repository
) : ViewModel() {

    fun createNewItem(toDoList: ToDoList) = repository.save(toDoList)

    fun getAdapterForRead(id: Int): EditReadAdapter = EditReadAdapter(repository.getItem(id)!!.toDoListItems)

    fun getAdapterForCreate(){}

    fun getItemById(id: Int) = repository.getItem(id)

}
