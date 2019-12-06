package com.meldeveloping.todowidget.model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.repository.Repository
import com.meldeveloping.todowidget.widget.WidgetProvider
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    fun removeItem(position: Int) {
        adapter.notifyItemChanged(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, list.size - 1)
        updateListPositionsDelete(position)
        repository.delete(list[position])
        list.removeAt(position)
        WidgetProvider.updateAppWidgets(context)
    }

    fun pinItem(position: Int) {
        repository.updatePositionInsert(0)
        list[position].toDoListPosition = 0
        list[position].isToDoListPinned = true
        repository.update(list[position])
    }

    fun unpinItem(position: Int) {
        repository.updatePositionDelete(0)
        list[position].isToDoListPinned = false
        list[position].toDoListPosition = getUnpinnedItemPosition(position)
        repository.update(list[position])
    }

    fun isToDoListPinned(position: Int): Boolean {
        return repository.getItem(list[position].id!!).isToDoListPinned
    }

    private fun initMainListAdapter() {
        list = repository.getAll()
        adapter = MainListAdapter(list)
    }

    private fun updateListPositionsDelete(position: Int) {
        repository.updatePositionDelete(position)
    }

    private fun getUnpinnedItemPosition(position: Int): Int {
        val result: Int
        var sortedPosition = 1
        val unpinnedItemsList = ArrayList<ToDoList>()

        for (item in list) {
            if (!item.isToDoListPinned) {
                unpinnedItemsList.add(item)
            }
        }
        unpinnedItemsList.sortWith(compareByDescending { getDateFromString(it.toDoListDate) })
        for ((a, item) in unpinnedItemsList.withIndex()) {
            if (item.id == list[position].id)
                sortedPosition = a
        }
        result = list.size - unpinnedItemsList.size
        return result + sortedPosition
    }

    private fun getDateFromString(date: String): Date {
        val formatter = SimpleDateFormat("dd.mm.yyyy hh:mm:ss", Locale.getDefault())
        return formatter.parse(date)
    }
}