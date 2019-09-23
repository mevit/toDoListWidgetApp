package com.meldeveloping.todowidget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.extension.toBoolean
import kotlinx.android.synthetic.main.todo_list_item.view.*

class EditReadAdapter(private val toDoListItems: ArrayList<ToDoListItem>) :
    RecyclerView.Adapter<EditReadAdapter.ListViewHolder>() {

    override fun getItemCount(): Int {
        return toDoListItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.view.itemCheckBox.isChecked = toDoListItems[position].isChecked.toBoolean()
        holder.view.itemText.text = toDoListItems[position].itemText
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}