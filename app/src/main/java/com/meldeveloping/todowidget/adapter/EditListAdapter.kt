package com.meldeveloping.todowidget.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.extension.toInt
import kotlinx.android.synthetic.main.edit_fragment_list_item.view.*

class EditListAdapter(private var toDoListItems: ArrayList<ToDoListItem>) :
    RecyclerView.Adapter<EditListAdapter.ListViewHolder>() {

    private lateinit var context: Context
    private var lastItemFocus = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
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

        holder.view.itemEditText.setOnFocusChangeListener { view, boolean ->
            if (boolean) {
                holder.view.itemEditText.setHintTextColor(ContextCompat.getColor(context, android.R.color.transparent))
            } else {
                holder.view.itemEditText.setHintTextColor(ContextCompat.getColor(context, R.color.hintColor))
            }
        }

        holder.view.itemEditText.setOnClickListener {
            setLastItemFocus(true)
            it.isFocusableInTouchMode = true
            it.requestFocus()
        }

        if (position == toDoListItems.size-1 && lastItemFocus) {
            holder.view.itemEditText.callOnClick()
        }
        setEditTextChangedListener(holder, position)
        setCheckBoxCheckedChangeListener(holder, position)
    }

    override fun getItemCount() = toDoListItems.size

    override fun getItemViewType(position: Int) = position

    override fun onViewDetachedFromWindow(holder: ListViewHolder) {
        super.onViewDetachedFromWindow(holder)

        if (holder.view.itemEditText.isFocused) {
            holder.view.itemEditText.isFocusable = false
        }
    }

    fun setLastItemFocus(focus: Boolean) {
        lastItemFocus = focus
    }

    fun getAdapterList() = toDoListItems

    private fun setEditTextChangedListener(holder: ListViewHolder, position: Int) {
        holder.view.itemEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                toDoListItems[position] =
                    ToDoListItem(toDoListItems[position].isChecked, p0.toString().trim())
            }
        })
    }

    private fun setCheckBoxCheckedChangeListener(holder: ListViewHolder, position: Int) {
        holder.view.itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            toDoListItems[position] =
                ToDoListItem(isChecked.toInt(), toDoListItems[position].itemText)
        }
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}