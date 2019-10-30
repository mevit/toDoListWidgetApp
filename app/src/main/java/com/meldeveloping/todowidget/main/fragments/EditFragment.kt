package com.meldeveloping.todowidget.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.model.EditViewModel
import kotlinx.android.synthetic.main.fragment_edit.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditFragment : Fragment() {

    companion object {

        private var toDoListId = MainActivity.DEFAULT_TODO_LIST_ID

        @JvmStatic
        fun newInstance(id: Int = MainActivity.DEFAULT_TODO_LIST_ID): EditFragment {
            toDoListId = id
            return EditFragment()
        }
    }

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

    override fun onStop() {
        super.onStop()
        saveList()
    }

    private fun initEditFragment() {
        toDoListItemsList.layoutManager = LinearLayoutManager(context)
        toDoListItemsList.adapter = editViewModel.getAdapterForRecycle(toDoListId)
        editViewModel.getItemTouchHelper().attachToRecyclerView(toDoListItemsList)
        titleEditText.setText(editViewModel.getToDoList().toDoListTitle)
    }

    private fun initButtons() {
        initNewItemButton()
    }

    private fun saveList() {
        editViewModel.getToDoList().toDoListTitle = titleEditText.text.toString()
        editViewModel.saveItem()
    }

    private fun initNewItemButton() {
        newItemButton.setOnClickListener {
            editViewModel.addEmptyItemToList()
            setFocusOnLastItem()
        }
    }

    private fun setFocusOnLastItem() {
        toDoListItemsList.smoothScrollToPosition(toDoListItemsList.adapter!!.itemCount - 1)
        editViewModel.getAdapter().lastItemFocusable(true)
    }
}
