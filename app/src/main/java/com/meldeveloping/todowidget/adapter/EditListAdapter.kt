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

        holder.view.itemEditText.setOnFocusChangeListener { view, boolean ->
            if (boolean) {
                holder.view.itemEditText.hint = ""
            } else {
                holder.view.itemEditText.hint = "do something…"
            }
        }

        holder.view.itemEditText.setOnClickListener {
            it.isFocusableInTouchMode = true
            it.requestFocus()
        }

        setLastItemFocus(holder)
        setEditTextChangedListener(holder, position)
        setCheckBoxCheckedChangeListener(holder, position)
    }

    override fun getItemCount() = toDoListItems.size

    override fun getItemViewType(position: Int) = position

    override fun onViewDetachedFromWindow(holder: ListViewHolder) {
        super.onViewDetachedFromWindow(holder)

        if (holder.view.itemEditText.isFocused)
            holder.view.itemEditText.isFocusable = false
    }

    fun getAdapterList() = toDoListItems

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