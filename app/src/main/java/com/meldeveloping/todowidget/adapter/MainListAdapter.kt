package com.meldeveloping.todowidget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.extension.toBoolean
import kotlinx.android.synthetic.main.main_fragment_list_item.view.*
import kotlin.collections.ArrayList

class MainListAdapter(
    private var toDoLists: ArrayList<ToDoList>,
    private var clickable: Boolean = true,
    private var longClickable: Boolean = true
) :
    RecyclerView.Adapter<MainListAdapter.ListViewHolder>() {

    init {
        toDoLists.sortWith(compareBy { it.toDoListPosition })
    }

    companion object {
        private const val DEFAULT_TO_DO_LIST_ID = -1
        var itemId: Int = DEFAULT_TO_DO_LIST_ID
        var itemPosition = 1
    }

    private lateinit var onClickListListener: View.OnClickListener
    private lateinit var onClickLongListListener: View.OnLongClickListener
    private lateinit var context: Context
    private var clickedPosition = DEFAULT_TO_DO_LIST_ID

    override fun getItemCount() = toDoLists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_fragment_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        setItemTitleText(holder, position)
        setItemDate(holder, position)
        setItemPinnedImageVisible(holder, position)
        setItemListeners(holder, position)
        if (clickedPosition == position) {
            holder.view.background =
                ContextCompat.getDrawable(context, R.drawable.img_main_item_bg_selected)
        } else {
            holder.view.background =
                ContextCompat.getDrawable(context, R.drawable.img_main_item_bg_light)
        }
    }

    fun setClickListener(listener: View.OnClickListener) {
        onClickListListener = listener
    }

    fun setLongClickListener(listener: View.OnLongClickListener) {
        onClickLongListListener = listener
    }

    fun selectItem(position: Int) {
        clickedPosition = position
        notifyDataSetChanged()
    }

    private fun setItemTitleText(holder: ListViewHolder, position: Int) {
        if (toDoLists[position].toDoListTitle == "") {
            holder.view.textViewListItem.text = toDoLists[position].toDoListItems[0].itemText
        } else {
            holder.view.textViewListItem.text = toDoLists[position].toDoListTitle
        }
    }

    private fun setItemDate(holder: ListViewHolder, position: Int) {
        holder.view.textViewListItemDate.text = toDoLists[position].toDoListDate.dropLast(3)
    }

    private fun setItemPinnedImageVisible(holder: ListViewHolder, position: Int) {
        if (toDoLists[position].isToDoListPinned)
            holder.view.pinItemImage.visibility = View.VISIBLE
    }

    private fun setItemListeners(holder: ListViewHolder, position: Int) {
        if (clickable)
            setItemClickListener(holder, toDoLists[position].id!!, position)
        if (longClickable)
            setItemLongClickListener(holder, toDoLists[position].id!!, position)
    }

    private fun setItemClickListener(holder: ListViewHolder, toDoListId: Int, position: Int) {
        holder.view.setOnClickListener {
            itemId = toDoListId
            itemPosition = position
            onClickListListener.onClick(it)
            clickedPosition = position
            notifyDataSetChanged()
        }
    }

    private fun setItemLongClickListener(holder: ListViewHolder, toDoListId: Int, position: Int) {
        holder.view.setOnLongClickListener {
            itemId = toDoListId
            itemPosition = position
            onClickLongListListener.onLongClick(it)
        }
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}