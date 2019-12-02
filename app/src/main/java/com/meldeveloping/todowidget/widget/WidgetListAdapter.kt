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
    private val intent: Intent
) : RemoteViewsService.RemoteViewsFactory, KoinComponent {

    private val repository: Repository by inject()
    private lateinit var toDoListItemsList: ArrayList<ToDoListItem>
    private val toDoListId: Int by lazy {
        intent.getIntExtra(WidgetProvider.TODO_LIST_ID, DEFAULT_LIST_ID)
    }

    companion object {
        const val DEFAULT_LIST_ID = 0
        private const val DEFAULT_ITEM_STYLE = R.layout.widget_list_item_dark
    }

    override fun onCreate() {}

    override fun getLoadingView() = null

    override fun getItemId(p0: Int) = p0.toLong()

    override fun onDataSetChanged() = initToDoListItemsList()

    override fun hasStableIds() = true

    override fun getViewAt(position: Int): RemoteViews {
        var checkedCheckBox = 0
        var uncheckedCheckBox = 0
        val style = intent.getIntExtra(WidgetProvider.TODO_LIST_STYLE, DEFAULT_ITEM_STYLE)
        val remoteViews = RemoteViews(context.packageName, style)
        val clickIntent = Intent()

        when (style) {
            R.layout.widget_list_item_light -> {
                checkedCheckBox = R.drawable.check_box_light_on
                uncheckedCheckBox = R.drawable.check_box_light_off
            }
            R.layout.widget_list_item_dark -> {
                checkedCheckBox = R.drawable.check_box_dark_on
                uncheckedCheckBox = R.drawable.check_box_dark_off
            }
            R.layout.widget_list_item_purple -> {
                checkedCheckBox = R.drawable.check_box_purple_on
                uncheckedCheckBox = R.drawable.check_box_purple_off
            }
        }

        initToDoListItemsList()
        remoteViews.setTextViewText(R.id.widgetItemText, toDoListItemsList[position].itemText)

        if (toDoListItemsList[position].isChecked.toBoolean()) {
            remoteViews.setImageViewResource(
                R.id.widgetCheckBox,
                checkedCheckBox
            )
        } else {
            remoteViews.setImageViewResource(
                R.id.widgetCheckBox,
                uncheckedCheckBox
            )
        }

        clickIntent.putExtra(WidgetProvider.TODO_LIST_ITEM_POSITION, position)
        clickIntent.putExtra(WidgetProvider.TODO_LIST_ID, toDoListId)
        remoteViews.setOnClickFillInIntent(R.id.widgetItemLayout, clickIntent)

        return remoteViews
    }

    override fun getCount() = toDoListItemsList.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {}

    private fun initToDoListItemsList() {
        if (repository.checkItem(toDoListId)) {
            toDoListItemsList = repository.getItem(toDoListId).toDoListItems
        } else {
            toDoListItemsList.clear()
        }
    }
}