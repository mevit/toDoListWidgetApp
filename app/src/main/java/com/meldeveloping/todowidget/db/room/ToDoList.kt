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
    var toDoListItems: ArrayList<ToDoListItem>,

    @ColumnInfo(name = "toDoListDate")
    var toDoListDate: String,

    @ColumnInfo(name = "toDoListPosition")
    var toDoListPosition: Int,

    @ColumnInfo(name = "isToDoListPinned")
    var isToDoListPinned: Boolean
)