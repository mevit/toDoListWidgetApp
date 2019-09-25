package com.meldeveloping.todowidget.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.meldeveloping.todowidget.db.ToDoListItem

@Entity
data class ToDoList(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "toDoListTitle")
    var toDoListTitle: String,

    @ColumnInfo(name = "toDoListItems")
    val toDoListItems: ArrayList<ToDoListItem>
)