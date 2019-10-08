package com.meldeveloping.todowidget.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.extension.showToast
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class WidgetListAdapter(val context: Context, val intent: Intent) :
    RemoteViewsService.RemoteViewsFactory, KoinComponent {

    private val repository: Repository by inject()
    private var list = repository.getAll()

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int) = p0.toLong()

    override fun onDataSetChanged() {
        list = repository.getAll()
    }

    override fun hasStableIds() = true

    override fun getViewAt(position: Int): RemoteViews {
        var remoteViews = RemoteViews(context.packageName, R.layout.todo_list_widget_item)
        remoteViews.setTextViewText(R.id.widgetItemText, list[0].toDoListItems[position].itemText)

        if (list[0].toDoListItems[position].isChecked.toBoolean()) {
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

        var clickIntent = Intent()
        clickIntent.putExtra(ToDoListWidget.ITEM_POSITION, position)
        remoteViews.setOnClickFillInIntent(R.id.widgetItemLayout, clickIntent)

        return remoteViews
    }

    override fun getCount() = list[0].toDoListItems.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {

    }
}