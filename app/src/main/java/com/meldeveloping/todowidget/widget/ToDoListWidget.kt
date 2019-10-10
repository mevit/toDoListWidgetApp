package com.meldeveloping.todowidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.RemoteViews
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ToDoListWidget : AppWidgetProvider(), KoinComponent {

    private val repository: Repository by inject()

    companion object {
        const val WIDGET_PREFERENCES = "widget_preferences"
        const val ACTION_CHECKBOX_CLICK = "checkbox_click"
        const val TODO_LIST_ID = "todo_list_id_"
        const val TODO_LIST_TITLE = "widget_title_"
        const val TODO_LIST_ITEM_POSITION = "todo_list_item_position"
        const val CHECKED_ITEM = 1
        const val UNCHECKED_ITEM = 0
        const val DEFAULT_WIDGET_TITLE_TEXT = "To do"
    }

    override fun onEnabled(context: Context) {}

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                getPreferences(context),
                appWidgetId
            )
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent!!.action.equals(ACTION_CHECKBOX_CLICK, true)) {
            val toDoListID = intent.getIntExtra(TODO_LIST_ID, 0)
            val itemPosition = intent.getIntExtra(TODO_LIST_ITEM_POSITION, -1)
            toDoListItemClick(toDoListID, itemPosition, context!!)
        }
    }

    override fun onDisabled(context: Context) {}

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)

        val editor = context!!.getSharedPreferences(
            WIDGET_PREFERENCES,
            Context.MODE_PRIVATE
        ).edit()
        for (appWidgetId in appWidgetIds!!) {
            editor.remove(TODO_LIST_ID + appWidgetId)
        }
        editor.apply()
    }

    fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, preferences: SharedPreferences, appWidgetId: Int) {
        val widgetView = RemoteViews(context.packageName, R.layout.to_do_list_widget)
        val toDoListId = preferences.getInt(TODO_LIST_ID + appWidgetId, 0)
        val toDoListTitle =
            preferences.getString(TODO_LIST_TITLE + appWidgetId, DEFAULT_WIDGET_TITLE_TEXT)
        widgetView.setTextViewText(R.id.widgetTitleTextView, toDoListTitle)
        widgetView.setOnClickPendingIntent(
            R.id.widgetTitleTextView,
            getPendingIntentMainActivity(context)
        )
        setListViewAdapter(context, widgetView, toDoListId)
        setListItemClickListener(widgetView, context)
        appWidgetManager.updateAppWidget(appWidgetId, widgetView)
    }

    private fun getPendingIntentMainActivity(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun setListViewAdapter(context: Context, views: RemoteViews, toDoListId: Int) {
        val intent = Intent(context, WidgetService::class.java).putExtra(TODO_LIST_ID, toDoListId)
        val data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        intent.data = data
        views.setRemoteAdapter(R.id.widgetListView, intent)
    }

    private fun setListItemClickListener(remoteViews: RemoteViews, context: Context) {
        val listClickIntent = Intent(context, ToDoListWidget::class.java)
        listClickIntent.action = ACTION_CHECKBOX_CLICK
        val listClickPendingIntent = PendingIntent.getBroadcast(context, 0, listClickIntent, 0)
        remoteViews.setPendingIntentTemplate(R.id.widgetListView, listClickPendingIntent)
    }

    private fun getPreferences(context: Context) = context.getSharedPreferences(WIDGET_PREFERENCES, Context.MODE_PRIVATE)

    private fun toDoListItemClick(toDoListId: Int, checkedItemPosition: Int, context: Context) {
        val toDoList = repository.getItem(toDoListId)
        val list = toDoList.toDoListItems

        if (checkedItemPosition != -1) {
            updateToDoListItemsList(list, checkedItemPosition)
            updateToDoList(toDoList, list)
            refreshWidgetListView(context)
        }
    }

    private fun updateToDoListItemsList(list: ArrayList<ToDoListItem>, checkedItemPosition: Int) {
        if (list[checkedItemPosition].isChecked.toBoolean()) {
            list[checkedItemPosition].isChecked = UNCHECKED_ITEM
        } else {
            list[checkedItemPosition].isChecked = CHECKED_ITEM
        }
    }

    private fun updateToDoList(toDoList: ToDoList, list: ArrayList<ToDoListItem>) {
        toDoList.toDoListItems = list
        repository.update(toDoList)
    }

    private fun refreshWidgetListView(context: Context) {
        val widgetManager = AppWidgetManager.getInstance(context)
        val widgetIds =
            widgetManager.getAppWidgetIds(ComponentName(context, ToDoListWidget::class.java))
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widgetListView)
    }
}

