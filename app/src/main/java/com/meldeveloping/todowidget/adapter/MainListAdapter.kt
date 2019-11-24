package com.meldeveloping.todowidget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.room.ToDoList
import kotlinx.android.synthetic.main.main_fragment_list_item.view.*

class MainListAdapter(private val toDoLists: ArrayList<ToDoList>) :
    RecyclerView.Adapter<MainListAdapter.ListViewHolder>() {

    companion object {
        private const val DEFAULT_TO_DO_LIST_ID = -1
        var itemId: Int = DEFAULT_TO_DO_LIST_ID
    }

    private lateinit var listListener: View.OnClickListener

    override fun getItemCount(): Int {
        return toDoLists.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_fragment_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.view.textViewListItem.text = toDoLists[position].toDoListTitle
        holder.view.textViewListItemDate.text = toDoLists[position].toDoListDate
        setItemClickListener(holder, toDoLists[position].id!!)
    }

    fun setClickListener(listener: View.OnClickListener) {
        listListener = listener
    }

    private fun setItemClickListener(holder: ListViewHolder, toDoListId: Int) {
        holder.view.setOnClickListener {
            itemId = toDoListId
            listListener.onClick(it)
        }
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}