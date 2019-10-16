package com.meldeveloping.todowidget.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.adapter.EditListAdapter
import com.meldeveloping.todowidget.model.EditViewModel
import kotlinx.android.synthetic.main.fragment_edit.*
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
        toDoListItemsList.layoutManager = LinearLayoutManager(context)
        toDoListItemsList.adapter = editViewModel.getAdapterForRecycle(toDoListId)
        editViewModel.getItemTouchHelper().attachToRecyclerView(toDoListItemsList)
        if (toDoListId == null) {
            initView(true)
        } else {
            initView(false)
        }
    }

    private fun initView(forEdit: Boolean) {
        if (forEdit) {
            editToolBar.text = ""
            editButton.visibility = View.GONE
            saveButton.visibility = View.VISIBLE
            newItemButton.visibility = View.VISIBLE
            titleEditText.visibility = View.VISIBLE
            EditListAdapter.ListViewHolder.isEditTextEnabled = true
            editViewModel.refreshAdapter()
            titleEditText.setText(editViewModel.getToDoList().toDoListTitle)
        } else {
            editButton.visibility = View.VISIBLE
            saveButton.visibility = View.GONE
            newItemButton.visibility = View.GONE
            titleEditText.visibility = View.GONE
            editToolBar.text = editViewModel.getToDoList().toDoListTitle
            EditListAdapter.ListViewHolder.isEditTextEnabled = false
            editViewModel.refreshAdapter()
        }
    }

    private fun initSaveButton() {
        saveButton.setOnClickListener {
            editViewModel.getToDoList().toDoListTitle = titleEditText.text.toString()
            editViewModel.saveItem()
            initView(forEdit = false)
        }
    }

    private fun initEditButton() {
        editButton.setOnClickListener {
            initView(true)
        }
    }

    private fun initNewItemButton() {
        newItemButton.setOnClickListener {
            editViewModel.addEmptyItemToList()
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
