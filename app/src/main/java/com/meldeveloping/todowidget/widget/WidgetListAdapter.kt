package com.meldeveloping.todowidget.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class WidgetListAdapter(val context: Context, val intent: Intent) : RemoteViewsService.RemoteViewsFactory, KoinComponent {

    private val repository: Repository by inject()

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int) = p0.toLong()

    override fun onDataSetChanged() {

    }

    override fun hasStableIds() = true

    override fun getViewAt(position: Int): RemoteViews {
        var remoteViews = RemoteViews(context.packageName, R.layout.todo_list_widget_item)
        remoteViews.setTextViewText(R.id.widgetItemText, repository.getItem(19)!!.toDoListItems[position].itemText)

        return remoteViews
    }

    override fun getCount() = repository.getItem(19)!!.toDoListItems.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {

    }
}