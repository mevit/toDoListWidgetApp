package com.meldeveloping.todowidget.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ToDoList::class], version = 1)
@TypeConverters(ItemConverter::class)
abstract class ToDoListDatabase : RoomDatabase() {

    abstract fun toDoListDao(): ToDoListDao

    companion object {
        const val DB_NAME: String = "toDoList.db"
    }

}