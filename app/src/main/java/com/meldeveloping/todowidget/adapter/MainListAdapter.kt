package com.meldeveloping.todowidget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.room.ToDoList
import kotlinx.android.synthetic.main.main_fragment_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainListAdapter(private var toDoLists: ArrayList<ToDoList>) :
    RecyclerView.Adapter<MainListAdapter.ListViewHolder>() {

    init {
        toDoLists.sortWith(compareBy {it.toDoListPosition})
    }

    companion object {
        private const val DEFAULT_TO_DO_LIST_ID = -1
        var itemId: Int = DEFAULT_TO_DO_LIST_ID
        var itemPosition = 1
    }

    private lateinit var onClickListListener: View.OnClickListener
    private lateinit var onClickLongListListener: View.OnLongClickListener

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
        holder.view.textViewListItemDate.text = toDoLists[position].toDoListDate.dropLast(3)
        if (toDoLists[position].isToDoListPinned)
            holder.view.pinItemImage.visibility = View.VISIBLE
        setItemClickListener(holder, toDoLists[position].id!!)
        setItemLongClickListener(holder, toDoLists[position].id!!, position)
    }

    fun setClickListener(listener: View.OnClickListener) {
        onClickListListener = listener
    }

    fun setLongClickListener(listener: View.OnLongClickListener) {
        onClickLongListListener = listener
    }

    private fun setItemClickListener(holder: ListViewHolder, toDoListId: Int) {
        holder.view.setOnClickListener {
            itemId = toDoListId
            onClickListListener.onClick(it)
        }
    }

    private fun setItemLongClickListener(holder: ListViewHolder, toDoListId: Int, position: Int) {
        holder.view.setOnLongClickListener {
            itemId = toDoListId
            itemPosition = position
            onClickLongListListener.onLongClick(it)
        }
    }

//    private fun getDateFromString(date: String): Date {
//        val formatter = SimpleDateFormat("dd.mm.yyyy hh:mm", Locale.GERMAN)
//        return formatter.parse(date)
//    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}