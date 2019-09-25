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
        initToolBar()
    }

    private fun initToolBar() {
        editButton.visibility = View.GONE
        saveButton.visibility = View.VISIBLE
        newItemButton.visibility = View.VISIBLE
        titleEditText.visibility = View.VISIBLE
    }

    private fun initSaveButton() {

        var list: ArrayList<ToDoListItem> = arrayListOf(
            ToDoListItem(1, "one"),
            ToDoListItem(0, "two"),
            ToDoListItem(0, "three"),
            ToDoListItem(0, "four")
        )

//        for (view in 0..toDoListItemsList.adapter!!.itemCount) {
//            Log.i("TAG", "---------------${toDoListItemsList.findViewHolderForAdapterPosition(view)!!.itemView.createItemCheckBox.isChecked}---------------")
//        }




        saveButton.setOnClickListener {
            EditViewModel.toDoList!!.toDoListTitle = titleEditText.text.toString()
            editViewModel.saveItem()
        }
    }

    private fun initEditButton() {
        editButton.setOnClickListener {
            initFragmentForEdit(editViewModel.getCreateAdapter())
            titleEditText.setText(EditViewModel.toDoList!!.toDoListTitle)
        }
    }

    private fun initNewItemButton() {
        newItemButton.setOnClickListener{
            editViewModel.addEmptyItemToList()
            toDoListItemsList.adapter = editViewModel.getCreateAdapter()
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
