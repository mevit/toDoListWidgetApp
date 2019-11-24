package com.meldeveloping.todowidget.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.adapter.EditListAdapter
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.repository.Repository
import com.meldeveloping.todowidget.widget.WidgetProvider
import java.text.SimpleDateFormat
import java.util.*

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

    fun getAdapterForRecycle(id: Int): EditListAdapter {
        return if (id == MainActivity.DEFAULT_TODO_LIST_ID) {
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

        if (toDoList.toDoListItems.size == 0 || toDoList == getEmptyList()) {
            repository.delete(toDoList)
        } else {
            toDoList.toDoListDate = getDate()

            if (toDoList.id == null) {
                repository.save(toDoList)
            } else {
                repository.update(toDoList)
            }
        }

        WidgetProvider.updateAppWidgets(context)
    }

    fun removeItem(position: Int) {
        toDoList.toDoListItems.removeAt(position)
        adapter.notifyItemRemoved(position)
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
                adapter.lastItemFocusable(false)
            }

        })
    }

    private fun initEmptyList() {
        toDoList = getEmptyList()
    }

    private fun initEditListAdapter(): EditListAdapter {
        adapter = EditListAdapter(toDoList.toDoListItems)
        return adapter
    }

    private fun initItemById(id: Int) {
        toDoList = repository.getItem(id)
    }

    private fun refreshAdapter() {
        adapter.notifyDataSetChanged()
    }

    private fun getEmptyList(): ToDoList {
        return ToDoList(
            toDoListTitle = EMPTY_ITEM_TEXT,
            toDoListItems = arrayListOf(ToDoListItem(EMPTY_ITEM_CHECKED, EMPTY_ITEM_TEXT)),
            toDoListDate = getDate()
        )
    }

    private fun getDate(): String {
        val date = Calendar.getInstance()
        val builder: StringBuilder = StringBuilder()
        builder
            .append(date.get(Calendar.DATE).toString())
            .append(".")
            .append(date.get(Calendar.MONTH).toString())
            .append(".")
            .append(date.get(Calendar.YEAR).toString())
        return builder.toString()
    }

}
