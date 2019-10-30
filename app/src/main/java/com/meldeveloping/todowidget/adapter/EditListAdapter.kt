package com.meldeveloping.todowidget.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.extension.toInt
import kotlinx.android.synthetic.main.edit_fragment_list_item.view.*

class EditListAdapter(private var toDoListItems: ArrayList<ToDoListItem>) :
    RecyclerView.Adapter<EditListAdapter.ListViewHolder>() {

    private var localToDoListItems = toDoListItems
    private var isFocusable = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.edit_fragment_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.view.itemCheckBox.isChecked = toDoListItems[position].isChecked.toBoolean()
        holder.view.itemEditText.setText(toDoListItems[position].itemText)

        setLastItemFocus(holder)
        setEditTextChangedListener(holder, position)
        setCheckBoxCheckedChangeListener(holder, position)
    }

    override fun getItemCount() = localToDoListItems.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    fun getLocalList() = localToDoListItems

    fun lastItemFocusable(isFocus: Boolean){
        isFocusable = isFocus
    }

    private fun setLastItemFocus(holder: ListViewHolder) {
        if (isFocusable) {
            holder.view.itemEditText.requestFocus()
        }
    }

    private fun setEditTextChangedListener(holder: ListViewHolder, position: Int) {
        holder.view.itemEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                localToDoListItems[position] =
                    ToDoListItem(localToDoListItems[position].isChecked, p0.toString())
            }
        })
    }

    private fun setCheckBoxCheckedChangeListener(holder: ListViewHolder, position: Int) {
        holder.view.itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            localToDoListItems[position] =
                ToDoListItem(isChecked.toInt(), localToDoListItems[position].itemText)
        }
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}