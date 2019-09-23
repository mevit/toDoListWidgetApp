package com.meldeveloping.todowidget.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.room.ToDoList
import kotlinx.android.synthetic.main.todo_list_item.view.*

class MainListAdapter(private val toDoLists: ArrayList<ToDoList>) :
    RecyclerView.Adapter<MainListAdapter.ListViewHolder>() {

    private var listListener: View.OnClickListener? = null

    override fun getItemCount(): Int {
        return toDoLists.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val holder = ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_list_item,
                parent,
                false
            )
        )

        if(listListener != null) {
            holder.view.setOnClickListener {
                listListener!!.onClick(it)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.view.textViewListItem.text = toDoLists[position].toDoListTitle
        holder.itemId = toDoLists[position].id
    }

    fun setClickListener(listener: View.OnClickListener) {
        listListener = listener
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        var itemId: Int? = null
    }

}