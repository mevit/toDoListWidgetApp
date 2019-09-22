package com.meldeveloping.todowidget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.room.ToDoList

class MainListAdapter(private val toDoLists: ArrayList<ToDoList>) : RecyclerView.Adapter<MainListAdapter.ListViewHolder>() {

    class ListViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item, parent, false) as TextView

        return ListViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return toDoLists.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.textView.text = toDoLists[position].toDoListTitle
    }



}