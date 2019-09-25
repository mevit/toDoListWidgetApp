package com.meldeveloping.todowidget.model

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.EditCreateAdapter
import com.meldeveloping.todowidget.adapter.EditReadAdapter
import com.meldeveloping.todowidget.adapter.ToDoAdapter
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.repository.Repository

class EditViewModel(
    private val repository: Repository
) : ViewModel() {

    companion object{
        private const val EMPTY_ITEM_CHECKED = 0
        private const val EMPTY_ITEM_TEXT = ""
    }

    fun createNewItem(toDoList: ToDoList) = repository.save(toDoList)

    fun getAdapterForRecycle(id: Int?): ToDoAdapter {
        return if (id == null) getCreateAdapter() else
            getReadAdapter(id)
    }

    fun getReadAdapter(id: Int) = EditReadAdapter(getItemById(id)!!.toDoListItems)

    fun getCreateAdapter(id: Int? = null): EditCreateAdapter {
        return if (id == null) {
            EditCreateAdapter(arrayListOf(ToDoListItem(EMPTY_ITEM_CHECKED, EMPTY_ITEM_TEXT)))
        } else {
            EditCreateAdapter(getItemById(id)!!.toDoListItems)
        }
    }

    fun getItemById(id: Int) = repository.getItem(id)

}
