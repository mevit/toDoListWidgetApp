package com.meldeveloping.todowidget.model

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.EditListAdapter
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.repository.Repository

class EditViewModel(
    private val repository: Repository
) : ViewModel() {

    companion object {
        private const val EMPTY_ITEM_CHECKED = 0
        private const val EMPTY_ITEM_TEXT = ""
    }

    private lateinit var adapter: EditListAdapter
    private lateinit var toDoList: ToDoList

    fun getAdapterForRecycle(id: Int?): EditListAdapter {
        return if (id == null) {
            initEmptyList()
            initEditListAdapter()
        } else {
            initItemById(id)
            initEditListAdapter()
        }
    }

    fun getToDoList() = toDoList

    fun getAdapter() = adapter

    fun refreshAdapter() {
        adapter.notifyDataSetChanged()
    }

    fun addEmptyItemToList() {
        toDoList.toDoListItems.add(ToDoListItem(EMPTY_ITEM_CHECKED, EMPTY_ITEM_TEXT))
    }

    fun saveItem() {
        toDoList.toDoListItems = adapter.getLocalList()
        if (toDoList.id == null) {
            repository.save(toDoList)
        } else {
            repository.update(toDoList)
        }
    }

    private fun initEditListAdapter(): EditListAdapter {
        adapter = EditListAdapter(toDoList.toDoListItems)

        adapter.setClickListener(View.OnClickListener {
            if(!EditListAdapter.ListViewHolder.isEditTextEnabled) {
                saveItem()
                refreshAdapter()
            }
        })

        return adapter
    }

    private fun initEmptyList() {
        toDoList = ToDoList(
            toDoListTitle = EMPTY_ITEM_TEXT,
            toDoListItems = arrayListOf(ToDoListItem(EMPTY_ITEM_CHECKED, EMPTY_ITEM_TEXT))
        )
    }

    private fun initItemById(id: Int) {
        toDoList = repository.getItem(id)!!
    }

}
