package com.meldeveloping.todowidget.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.adapter.EditCreateAdapter
import com.meldeveloping.todowidget.adapter.EditReadAdapter
import com.meldeveloping.todowidget.adapter.ToDoAdapter
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.model.EditViewModel
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.todo_list_create_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditFragment : Fragment() {

    private val editViewModel: EditViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEditFragment()
        initButtons()
    }

    private fun initButtons() {
        initSaveButton()
        initEditButton()
        initNewItemButton()
    }

    private fun initEditFragment() {
        when (val adapter: ToDoAdapter = editViewModel.getAdapterForRecycle(toDoListId)) {
            is EditCreateAdapter -> initFragmentForEdit(adapter)
            is EditReadAdapter -> initFragmentForRead(adapter)
        }
    }

    private fun initFragmentForRead(adapter: EditReadAdapter) {
        editToolBar.text = EditViewModel.toDoList!!.toDoListTitle
        toDoListItemsList.layoutManager = LinearLayoutManager(context)
        toDoListItemsList.adapter = adapter
    }

    private fun initFragmentForEdit(adapter: EditCreateAdapter) {
        editToolBar.text = ""
        toDoListItemsList.layoutManager = LinearLayoutManager(context)
        toDoListItemsList.adapter = adapter
        initToolBar(true)
    }

    private fun initToolBar(forEdit: Boolean) {
        if (forEdit) {
            editButton.visibility = View.GONE
            saveButton.visibility = View.VISIBLE
            newItemButton.visibility = View.VISIBLE
            titleEditText.visibility = View.VISIBLE
        } else {
            editButton.visibility = View.VISIBLE
            saveButton.visibility = View.GONE
            newItemButton.visibility = View.GONE
            titleEditText.visibility = View.GONE
        }
    }

    private fun initSaveButton() {
        saveButton.setOnClickListener {
            EditViewModel.toDoList!!.toDoListTitle = titleEditText.text.toString()
            editViewModel.saveItem()
            initFragmentForRead(editViewModel.getReadAdapter())
            initToolBar(false)
        }
    }

    private fun initEditButton() {
        editButton.setOnClickListener {
            initFragmentForEdit(editViewModel.getCreateAdapter())
            titleEditText.setText(EditViewModel.toDoList!!.toDoListTitle)
        }
    }

    private fun initNewItemButton() {
        newItemButton.setOnClickListener {
            editViewModel.addEmptyItemToList()
            toDoListItemsList.adapter!!.notifyDataSetChanged()
        }
    }

    companion object {

        private var toDoListId: Int? = null

        @JvmStatic
        fun newInstance(id: Int? = null): EditFragment {
            toDoListId = id
            return EditFragment()
        }
    }

}
