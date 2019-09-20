package com.meldeveloping.todowidget.repository

import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.db.room.ToDoListDao

interface Repository {
    val toDoListDao: ToDoListDao

    fun save()
    fun getAll(): ArrayList<ToDoList>
}