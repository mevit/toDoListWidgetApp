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
import kotlinx.android.synthetic.main.todo_list_create_item.view.*

class EditCreateAdapter(private var toDoListItems: ArrayList<ToDoListItem>) :
    RecyclerView.Adapter<EditCreateAdapter.ListViewHolder>(), ToDoAdapter {

    private var localToDoListItems = toDoListItems

    fun getNewListForAdapter() = localToDoListItems

    override fun getItemCount() = toDoListItems.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_list_create_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.view.createItemCheckBox.isChecked = toDoListItems[position].isChecked.toBoolean()
        holder.view.createItemEditText.setText(toDoListItems[position].itemText)

        holder.view.createItemEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                localToDoListItems[position] = ToDoListItem(localToDoListItems[position].isChecked, p0.toString())
            }
        })

        holder.view.createItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            saveItemChecked(isChecked, position)
        }
    }

    private fun saveItemChecked(isChecked: Boolean, position: Int) {
        localToDoListItems[position] = ToDoListItem(isChecked.toInt(), localToDoListItems[position].itemText)
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}