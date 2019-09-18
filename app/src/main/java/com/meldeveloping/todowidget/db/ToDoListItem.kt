package com.meldeveloping.todowidget.db

data class ToDoListItem(
    val isChecked: Boolean = false,
    val itemText: String)