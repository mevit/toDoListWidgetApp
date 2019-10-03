package com.meldeveloping.todowidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.main.MainActivity

class ToDoListWidget : AppWidgetProvider() {

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

    companion object {

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
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
            val intent = Intent(context, WidgetService::class.java)
            views.setRemoteAdapter(R.id.widgetListView, intent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        /*
        function for open activity
         */
        private fun getPendingIntent(context: Context, value: Int): PendingIntent {
            val intent = Intent(context, MainActivity::class.java)
//            //2
//            intent.action = Constants.ADD_COFFEE_INTENT
//            //3
//            intent.putExtra(Constants.GRAMS_EXTRA, value)
//            //4
            return PendingIntent.getActivity(context, value, intent, 0)
        }
    }
}

