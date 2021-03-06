package com.meldeveloping.todowidget.repository

import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.db.room.ToDoListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RoomDBRepository(override val toDoListDao: ToDoListDao) : Repository {

    override fun updatePositionDelete(position: Int) {
        runBlocking {
            launch(Dispatchers.Default) {
                toDoListDao.updatePositionDelete(position)
            }
        }
    }

    override fun updatePositionInsert(position: Int) {
        runBlocking {
            launch(Dispatchers.Default) {
                toDoListDao.updatePositionInsert(position)
            }
        }
    }

    override fun checkItem(id: Int): Boolean {
        var isExist = false
        runBlocking {
            launch(Dispatchers.Default) {
               isExist = toDoListDao.checkItem(id)
            }
        }
        return isExist
    }

    override fun getAll(): ArrayList<ToDoList> {
        var allToDoLists = ArrayList<ToDoList>()
        runBlocking {
            launch(Dispatchers.Default) {
                allToDoLists = ArrayList(toDoListDao.getAll()!!.toMutableList())
            }
        }
        return allToDoLists
    }

    override fun save(toDoList: ToDoList) {
        runBlocking {
            launch(Dispatchers.Default) {
                toDoListDao.insert(toDoList)
            }
        }
    }

    override fun update(toDoList: ToDoList) {
        runBlocking {
            launch(Dispatchers.Default) {
                toDoListDao.update(toDoList)
            }
        }
    }

    override fun delete(toDoList: ToDoList) {
        runBlocking {
            launch(Dispatchers.Default) {
                toDoListDao.delete(toDoList)
            }
        }
    }

    override fun getItem(id: Int): ToDoList {
        lateinit var item: ToDoList
        runBlocking {
            launch(Dispatchers.Default) {
                item = toDoListDao.getItem(id)
            }
        }
        return item
    }
}