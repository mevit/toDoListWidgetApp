package com.meldeveloping.todowidget.db.room

import androidx.room.*

@Dao
interface ToDoListDao {

    @Query("SELECT * FROM toDoList")
    fun getAll(): List<ToDoList>?

    @Query("SELECT * FROM toDoList WHERE id = :id")
    fun getItem(id: Int): ToDoList

    @Query("SELECT EXISTS(SELECT * FROM toDoList WHERE id = :id)")
    fun checkItem(id: Int): Boolean

    @Insert
    fun insert(toDoList: ToDoList)

    @Update
    fun update(toDoList: ToDoList)

    @Delete
    fun delete(toDoList: ToDoList)

}