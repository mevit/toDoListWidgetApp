package com.meldeveloping.todowidget.model

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.adapter.EditListAdapter
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.repository.Repository
import com.meldeveloping.todowidget.widget.ToDoListWidget

class EditViewModel(
    private val repository: Repository,
    private val context: Context
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

    fun getAdapter() = adapter

    fun getToDoList() = toDoList

    fun addEmptyItemToList() {
        toDoList.toDoListItems.add(ToDoListItem(EMPTY_ITEM_CHECKED, EMPTY_ITEM_TEXT))
        adapter.notifyItemInserted(toDoList.toDoListItems.size)
    }

    fun saveItem() {
        toDoList.toDoListItems = adapter.getLocalList()

        if (toDoList.toDoListItems.size == 0) {
            repository.delete(toDoList)
        } else {
            if (toDoList.id == null) {
                repository.save(toDoList)
            } else {
                repository.update(toDoList)
            }
        }

        ToDoListWidget.refreshWidget(context)
    }

    fun removeItem(position: Int) {
        toDoList.toDoListItems.removeAt(position)
        refreshAdapter()
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeItem(viewHolder.adapterPosition)
                adapter.setLastItemFocusable(false)
            }

        })
    }

    private fun refreshAdapter() {
        adapter.notifyDataSetChanged()
    }

    private fun initEditListAdapter(): EditListAdapter {
        adapter = EditListAdapter(toDoList.toDoListItems)

        adapter.setClickListener(View.OnClickListener {
            saveItem()
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
        toDoList = repository.getItem(id)
    }

}
