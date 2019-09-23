package com.meldeveloping.todowidget.db.room

import androidx.room.*

@Dao
interface ToDoListDao {

    @Query("SELECT * FROM toDoList")
    fun getAll(): List<ToDoList>?



    @Insert
    fun insert(toDoList: ToDoList)

    @Update
    fun update(toDoList: ToDoList)

    @Delete
    fun delete(toDoList: ToDoList)

}