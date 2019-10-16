package com.meldeveloping.todowidget.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class WidgetListAdapter(
    val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory, KoinComponent {

    private val repository: Repository by inject()
    private lateinit var toDoListItemsList: ArrayList<ToDoListItem>
    private val toDoListId: Int by lazy {
        intent.getIntExtra(ToDoListWidget.TODO_LIST_ID, 0)
    }

    override fun onCreate() {}

    override fun getLoadingView() = null

    override fun getItemId(p0: Int) = p0.toLong()

    override fun onDataSetChanged() = initToDoListItemsList()

    override fun hasStableIds() = true

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.todo_list_widget_item)
        val clickIntent = Intent()

        initToDoListItemsList()
        remoteViews.setTextViewText(R.id.widgetItemText, toDoListItemsList[position].itemText)

        if (toDoListItemsList[position].isChecked.toBoolean()) {
            remoteViews.setImageViewResource(
                R.id.widgetCheckBox,
                android.R.drawable.checkbox_on_background
            )
        } else {
            remoteViews.setImageViewResource(
                R.id.widgetCheckBox,
                android.R.drawable.checkbox_off_background
            )
        }

        clickIntent.putExtra(ToDoListWidget.TODO_LIST_ITEM_POSITION, position)
        clickIntent.putExtra(ToDoListWidget.TODO_LIST_ID, toDoListId)
        remoteViews.setOnClickFillInIntent(R.id.widgetItemLayout, clickIntent)

        return remoteViews
    }

    override fun getCount() = toDoListItemsList.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {}

    private fun initToDoListItemsList() {
        toDoListItemsList = repository.getItem(toDoListId).toDoListItems
    }
}