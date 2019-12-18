package com.meldeveloping.todowidget.db

data class ToDoListItem(
    var isChecked: Int = 0,
    val itemText: String)