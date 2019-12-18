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

    @Query("UPDATE toDoList SET toDoListPosition = toDoListPosition + 1 WHERE toDoListPosition >= :position")
    fun updatePositionInsert(position: Int)

    @Query("UPDATE toDoList SET toDoListPosition = toDoListPosition - 1 WHERE toDoListPosition > :position")
    fun updatePositionDelete(position: Int)

    @Insert
    fun insert(toDoList: ToDoList)

    @Update
    fun update(toDoList: ToDoList)

    @Delete
    fun delete(toDoList: ToDoList)
}