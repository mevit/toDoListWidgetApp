package com.meldeveloping.todowidget.main.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.extension.showLog
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

    override fun onStop() {
        super.onStop()
        saveList()
    }

    private fun initButtons() {
        initNewItemButton()
    }

    private fun saveList() {
        editViewModel.getToDoList().toDoListTitle = titleEditText.text.toString()
        editViewModel.saveItem()
    }

    private fun initEditFragment() {
        toDoListItemsList.layoutManager = LinearLayoutManager(context)
        toDoListItemsList.adapter = editViewModel.getAdapterForRecycle(toDoListId)
        editViewModel.getItemTouchHelper().attachToRecyclerView(toDoListItemsList)
        titleEditText.setText(editViewModel.getToDoList().toDoListTitle)
    }

    private fun initNewItemButton() {
        newItemButton.setOnClickListener {
            editViewModel.addEmptyItemToList()
            setFocusOnLastItem()
        }
    }

    private fun setFocusOnLastItem() {
        toDoListItemsList.smoothScrollToPosition(toDoListItemsList.adapter!!.itemCount - 1)
        //refactor
        if(!editViewModel.getAdapter().isShowKeyboard()){
            showKeyboard()
        }
        editViewModel.getAdapter().setLastItemFocusable(true)
    }

    private fun showKeyboard() {
        val inputManager =
            context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
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
