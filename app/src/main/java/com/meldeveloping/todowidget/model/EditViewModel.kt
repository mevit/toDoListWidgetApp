package com.meldeveloping.todowidget.model

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.repository.Repository

class EditViewModel(
    private val repository: Repository
) : ViewModel() {

    fun createNewItem(toDoList: ToDoList){

//        var list: ArrayList<ToDoListItem> = arrayListOf(
//            ToDoListItem(1, "one"),
//            ToDoListItem(0, "two"),
//            ToDoListItem(0, "three"),
//            ToDoListItem(0, "four")
//        )
//
//        ToDoList(toDoListTitle = "one", toDoListItems = list)

        repository.save(toDoList)
    }

}
