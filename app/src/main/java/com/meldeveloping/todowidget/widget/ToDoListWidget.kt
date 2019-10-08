package com.meldeveloping.todowidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.extension.showToast
import com.meldeveloping.todowidget.extension.toBoolean
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ToDoListWidget : AppWidgetProvider(), KoinComponent {

    private val repository: Repository by inject()

    companion object {
        const val CHECKBOX_CLICK = "CHECKBOX CLICK"
        const val ITEM_POSITION = "ITEM_POSITION"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {}

    override fun onDisabled(context: Context) {}

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        var list = repository.getAll()[0].toDoListItems
        var toDoList = repository.getItem(19)

        if (intent!!.action.equals(CHECKBOX_CLICK, true)) {
            var position = intent.getIntExtra(ITEM_POSITION, -1)

            // instruments for updating listview
            var widgetManager = AppWidgetManager.getInstance(context)
            var widgetIds =
                widgetManager.getAppWidgetIds(ComponentName(context!!, ToDoListWidget::class.java))

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
                widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widgetListView)
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        val views = RemoteViews(
            context.packageName,
            R.layout.to_do_list_widget
        )
        views.setOnClickPendingIntent(
            R.id.widgetEditButton,
            getPendingIntent(
                context,
                0
            )
        )

        // set adapter for listview
        val intent = Intent(context, WidgetService::class.java)
        views.setRemoteAdapter(R.id.widgetListView, intent)

        listItemClick(views, context)

        appWidgetManager.updateAppWidget(appWidgetId, views)
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
}

