package com.meldeveloping.todowidget.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.model.EditViewModel
import kotlinx.android.synthetic.main.fragment_edit.*
import org.koin.android.ext.android.inject

class EditFragment(private val toDoList: ToDoList? = null) : Fragment() {

    private val editViewModel: EditViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveNewItem()
    }

    private fun saveNewItem() {

        var list: ArrayList<ToDoListItem> = arrayListOf(
            ToDoListItem(1, "one"),
            ToDoListItem(0, "two"),
            ToDoListItem(0, "three"),
            ToDoListItem(0, "four")
        )

        saveButton.setOnClickListener {
            editViewModel.createNewItem(
                ToDoList(toDoListTitle = titleEditText.text.toString(), toDoListItems = list)
            )
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = EditFragment()
    }

}
