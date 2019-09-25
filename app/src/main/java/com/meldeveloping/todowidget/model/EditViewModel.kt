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

    companion object {
        private const val EMPTY_ITEM_CHECKED = 0
        private const val EMPTY_ITEM_TEXT = ""

        var toDoList: ToDoList? = null
    }

    fun getAdapterForRecycle(id: Int?): ToDoAdapter {
        return if (id == null) {
            initEmptyList()
            getCreateAdapter()
        } else {
            initItemById(id)
            getReadAdapter()
        }
    }

    fun getReadAdapter() = EditReadAdapter(toDoList!!.toDoListItems)

    fun getCreateAdapter() = EditCreateAdapter(toDoList!!.toDoListItems)

    private fun initEmptyList() {
        toDoList = ToDoList(
            toDoListTitle = EMPTY_ITEM_TEXT,
            toDoListItems = arrayListOf(ToDoListItem(EMPTY_ITEM_CHECKED, EMPTY_ITEM_TEXT))
        )
    }

    fun addEmptyItemToList() {
        toDoList!!.toDoListItems.add(ToDoListItem(EMPTY_ITEM_CHECKED, EMPTY_ITEM_TEXT))
    }

    private fun initItemById(id: Int) {
        toDoList = repository.getItem(id)
    }

    fun saveItem() = repository.save(toDoList!!)

}
