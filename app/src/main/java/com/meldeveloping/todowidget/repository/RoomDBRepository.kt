package com.meldeveloping.todowidget.repository

import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.db.room.ToDoListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RoomDBRepository(override val toDoListDao: ToDoListDao) : Repository {

    override fun getAll(): ArrayList<ToDoList> {
        var allToDoLists = ArrayList<ToDoList>()
        runBlocking {
            launch(Dispatchers.Default) {
                allToDoLists = ArrayList(toDoListDao.getAll()!!.toMutableList())
            }
        }
        return allToDoLists
    }

    override fun save() {

        var list: ArrayList<ToDoListItem> = arrayListOf(
            ToDoListItem(1, "one"),
            ToDoListItem(0, "two"),
            ToDoListItem(0, "three"),
            ToDoListItem(0, "four")
        )

        runBlocking {
            launch(Dispatchers.Default) {
                toDoListDao.insert(
                    ToDoList(toDoListTitle = "one", toDoListItems = list)
                )
            }
        }
    }

}