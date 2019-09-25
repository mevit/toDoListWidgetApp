package com.meldeveloping.todowidget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.extension.toBoolean
import kotlinx.android.synthetic.main.todo_list_create_item.view.*

class EditCreateAdapter(private val toDoListItems: ArrayList<ToDoListItem>) :
    RecyclerView.Adapter<EditCreateAdapter.ListViewHolder>(), ToDoAdapter {

    override fun getItemCount() = toDoListItems.size

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
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}