package com.meldeveloping.todowidget.main.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.extension.isKeyboardVisible
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.model.EditViewModel
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_main.*
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

    override fun onDestroyView() {
        super.onDestroyView()

        hideKeyboard()
        saveList()
    }

    private fun initEditFragment() {
        toDoListItemsList.layoutManager = LinearLayoutManager(context)
        toDoListItemsList.adapter = editViewModel.getAdapterForRecycle(toDoListId)
        editViewModel.getItemTouchHelper().attachToRecyclerView(toDoListItemsList)
        titleEditText.setText(editViewModel.getToDoList().toDoListTitle)
        titleEditText.setOnFocusChangeListener { view, boolean ->
            if (boolean) {
                titleEditText.hint = ""
            } else {
                titleEditText.hint = getString(R.string.empty_title_hint_text)
            }
        }
    }

    private fun initButtons() {
        initNewItemButton()
        initBackButton()
    }

    private fun saveList() {
        editViewModel.getToDoList().toDoListTitle = titleEditText.text.toString().trim()
        editViewModel.saveItem()
    }

    private fun initNewItemButton() {
        newItemButton.setOnClickListener {
            editViewModel.addEmptyItemToList()
            setFocusOnLastItem()
        }
    }

    private fun initBackButton() {
        backButton.setOnClickListener {
            activity!!.onBackPressed()
        }
    }

    private fun setFocusOnLastItem() {
        toDoListItemsList.smoothScrollToPosition(toDoListItemsList.adapter!!.itemCount - 1)
        editViewModel.getAdapter().lastItemFocusable(true)
    }

    private fun hideKeyboard() {
        val keyboard =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isKeyboardVisible(activity!!))
            keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}
