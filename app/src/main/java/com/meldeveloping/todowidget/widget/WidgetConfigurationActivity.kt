package com.meldeveloping.todowidget.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.model.WidgetConfigViewModel
import kotlinx.android.synthetic.main.activity_widget_config.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WidgetConfigurationActivity : AppCompatActivity() {

    private val configViewModel: WidgetConfigViewModel by viewModel()
    private var widgetID = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var resultIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        initWidgetID()
        initResultIntent()
        initWidgetConfigList()
        setResult(Activity.RESULT_CANCELED, resultIntent)
    }

    private fun initWidgetID() {
        val extras = intent.extras
        if (extras != null) {
            widgetID = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish()
    }

    private fun initResultIntent() {
        resultIntent = Intent()
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID)
    }

    private fun initWidgetConfigList() {
        if (configViewModel.getListAdapter().itemCount != 0) {
            initList(configViewModel.getListAdapter())
            configViewModel.setAdapterListener { configWidget(MainListAdapter.itemId!!) }
        } else {
            configWidgetEmptyButton.visibility = View.VISIBLE
        }
    }

    private fun initList(adapter: MainListAdapter) {
        configWidgetList.layoutManager = LinearLayoutManager(applicationContext)
        configWidgetList.adapter = adapter
    }

    private fun configWidget(id: Int) {
        val preferences = createPreferences(id)
        updateWidget(preferences)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun createPreferences(id: Int): SharedPreferences {
        val preferences =
            getSharedPreferences(ToDoListWidget.WIDGET_PREFERENCES, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(ToDoListWidget.TODO_LIST_ID + widgetID, id)
        editor.putString(
            ToDoListWidget.TODO_LIST_TITLE + widgetID,
            configViewModel.getToDoListTitle(id)
        )
        editor.apply()
        return preferences
    }

    private fun updateWidget(preferences: SharedPreferences) {
        val widgetManager = AppWidgetManager.getInstance(applicationContext)
        val widget = ToDoListWidget()
        widget.updateAppWidget(applicationContext, widgetManager, preferences, widgetID)
    }
}