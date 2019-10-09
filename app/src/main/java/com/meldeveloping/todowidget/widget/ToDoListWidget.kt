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
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ToDoListWidget : AppWidgetProvider(), KoinComponent {

    private val repository: Repository by inject()
    private var toDoListID = 0

    companion object {
        const val CHECKBOX_CLICK = "CHECKBOX CLICK"
        const val ITEM_POSITION = "ITEM_POSITION"
        const val TODO_LIST_ID = "todo_list_id"
    }

    override fun onEnabled(context: Context) {}

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
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

        var toDoList = repository.getItem(19)
        var list = toDoList.toDoListItems

        if (intent!!.action.equals(CHECKBOX_CLICK, true)) {
            var position = intent.getIntExtra(ITEM_POSITION, -1)


            if (position != -1) {

                //change isChecked
                if (list[position].isChecked.toBoolean()) {
                    list[position].isChecked = 0
                } else {
                    list[position].isChecked = 1
                }

                toDoList.toDoListItems = list
                repository.update(toDoList)

                // update listview
                refreshWidgetList(context!!)
            }
        }
    }

    override fun onDisabled(context: Context) {}

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)

        var editor = context!!.getSharedPreferences(
            WidgetConfigurationActivity.WIDGET_PREFERENCES,
            Context.MODE_PRIVATE
        ).edit()
        for (appWidgetId in appWidgetIds!!) {
            editor.remove(WidgetConfigurationActivity.WIDGET_ITEM + appWidgetId)
        }
        editor.apply()
    }

    fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        preferences: SharedPreferences,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.to_do_list_widget)
        toDoListID = preferences.getInt(WidgetConfigurationActivity.WIDGET_ITEM + appWidgetId, 0)
        views.setOnClickPendingIntent(
            R.id.widgetEditButton,
            getPendingIntent(context, 0)
        )
        setListViewAdapter(context, views)
        listItemClick(views, context)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun setListViewAdapter(context: Context, views: RemoteViews) {
        val intent = Intent(context, WidgetService::class.java)
        intent.putExtra(TODO_LIST_ID, toDoListID)
        val data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        intent.data = data
        views.setRemoteAdapter(R.id.widgetListView, intent)
    }

    private fun listItemClick(remoteViews: RemoteViews, context: Context) {
        val listClickIntent = Intent(context, ToDoListWidget::class.java)
        listClickIntent.action = CHECKBOX_CLICK
        val listClickPendingIntent = PendingIntent.getBroadcast(context, 0, listClickIntent, 0)
        remoteViews.setPendingIntentTemplate(R.id.widgetListView, listClickPendingIntent)
    }

    private fun getPendingIntent(context: Context, value: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, value, intent, 0)
    }

    private fun refreshWidgetList(context: Context) {
        val widgetManager = AppWidgetManager.getInstance(context)
        val widgetIds =
            widgetManager.getAppWidgetIds(ComponentName(context!!, ToDoListWidget::class.java))
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widgetListView)
    }

    private fun getPreferences(context: Context) = context.getSharedPreferences(
        WidgetConfigurationActivity.WIDGET_PREFERENCES,
        Context.MODE_PRIVATE
    )
}

