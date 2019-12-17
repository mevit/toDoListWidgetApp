package com.meldeveloping.todowidget.repository

import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.db.room.ToDoListDao

interface Repository {
    val toDoListDao: ToDoListDao

    fun save(toDoList: ToDoList)
    fun getAll(): ArrayList<ToDoList>
    fun getItem(id: Int): ToDoList
    fun update(toDoList: ToDoList)
    fun delete(toDoList: ToDoList)
    fun checkItem(id: Int): Boolean
    fun updatePositionInsert(position: Int)
    fun updatePositionDelete(position: Int)
}