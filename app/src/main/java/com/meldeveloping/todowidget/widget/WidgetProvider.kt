package com.meldeveloping.todowidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.db.ToDoListItem
import com.meldeveloping.todowidget.db.room.ToDoList
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class WidgetProvider : AppWidgetProvider(), KoinComponent {

    private val repository: Repository by inject()

    companion object {
        const val WIDGET_PREFERENCES = "widget_preferences"
        const val ACTION_CHECKBOX_CLICK = "checkbox_click"
        const val TODO_LIST_ID = "todo_list_id_"
        const val TODO_LIST_STYLE = "todo_list_style_"
        const val TODO_LIST_ITEM_POSITION = "todo_list_item_position"
        const val CHECKED_ITEM = 1
        const val UNCHECKED_ITEM = 0
        private const val DELETED_TODO_LIST = "Your list deleted"

        fun updateAppWidgets(context: Context) {
            val widgetManager = AppWidgetManager.getInstance(context)
            val widgetIds =
                widgetManager.getAppWidgetIds(ComponentName(context, WidgetProvider::class.java))
            widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widgetListView)

            for (appWidgetId in widgetIds) {
                updateAppWidget(
                    context,
                    widgetManager,
                    context.getSharedPreferences(WIDGET_PREFERENCES, Context.MODE_PRIVATE),
                    appWidgetId
                )
            }
        }

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            preferences: SharedPreferences,
            appWidgetId: Int
        ) {
            val style =
                preferences.getInt(TODO_LIST_STYLE + appWidgetId, R.layout.widget_view_light)
            val widgetView = RemoteViews(context.packageName, style)
            val toDoListId = preferences.getInt(TODO_LIST_ID + appWidgetId, 1)
            var toDoListTitle = DELETED_TODO_LIST
            val toDoListWidget = WidgetProvider()

            if (toDoListWidget.repository.checkItem(toDoListId)) {
                toDoListTitle = toDoListWidget.repository.getItem(toDoListId).toDoListTitle
                widgetView.setTextViewText(R.id.widgetTitleTextView, toDoListTitle)
                widgetView.setOnClickPendingIntent(
                    R.id.widgetTitleTextView,
                    toDoListWidget.getPendingIntentMainActivity(context, toDoListId, appWidgetId)
                )
                if (style == R.layout.widget_view_light) {
                    toDoListWidget.setListViewAdapter(context, widgetView, toDoListId, R.layout.widget_list_item_light)
                } else {
                    toDoListWidget.setListViewAdapter(context, widgetView, toDoListId, R.layout.widget_list_item_dark)
                }
                toDoListWidget.setListItemClickListener(widgetView, context)
                widgetView.setViewVisibility(R.id.widgetNewListButton, View.GONE)
            } else {
                widgetView.setTextViewText(R.id.widgetTitleTextView, toDoListTitle)
                widgetView.setOnClickPendingIntent(R.id.widgetTitleTextView, null)
                widgetView.setViewVisibility(R.id.widgetNewListButton, View.VISIBLE)
                widgetView.setOnClickPendingIntent(
                    R.id.widgetNewListButton,
                    toDoListWidget.getPendingIntentConfigActivity(context, appWidgetId)
                )
            }

            appWidgetManager.updateAppWidget(appWidgetId, widgetView)
        }
    }

    override fun onEnabled(context: Context) {}

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateAppWidgets(context)
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

    private fun getPendingIntentMainActivity(
        context: Context,
        toDoListId: Int,
        id: Int
    ): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.OPEN_EDIT_FRAGMENT, toDoListId)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getPendingIntentConfigActivity(context: Context, id: Int): PendingIntent {
        val intent = Intent(context, WidgetConfigurationActivity::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun setListViewAdapter(context: Context, views: RemoteViews, toDoListId: Int, widgetStyle: Int) {
        val intent = Intent(context, WidgetService::class.java)
        intent.putExtra(TODO_LIST_ID, toDoListId)
        intent.putExtra(TODO_LIST_STYLE, widgetStyle)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        views.setRemoteAdapter(R.id.widgetListView, intent)
    }

    private fun setListItemClickListener(remoteViews: RemoteViews, context: Context) {
        val listClickIntent = Intent(context, WidgetProvider::class.java)
        listClickIntent.action = ACTION_CHECKBOX_CLICK
        val listClickPendingIntent = PendingIntent.getBroadcast(context, 0, listClickIntent, 0)
        remoteViews.setPendingIntentTemplate(R.id.widgetListView, listClickPendingIntent)
    }

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
            widgetManager.getAppWidgetIds(ComponentName(context, WidgetProvider::class.java))
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widgetListView)
    }
}