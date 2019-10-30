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
import com.meldeveloping.todowidget.main.MainActivity
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
        initNewButton()
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

    private fun initNewButton() {
        configWidgetEmptyButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initWidgetConfigList() {
        if (configViewModel.getListAdapter().itemCount != 0) {
            configWidgetEmptyButton.visibility = View.GONE
            initList(configViewModel.getListAdapter())
            setAdapterListener(configViewModel.getAdapter())
        } else {
            configWidgetEmptyButton.visibility = View.VISIBLE
        }
    }

    private fun initList(adapter: MainListAdapter) {
        configWidgetList.layoutManager = LinearLayoutManager(applicationContext)
        configWidgetList.adapter = adapter
    }

    private fun setAdapterListener(adapter: MainListAdapter) {
        adapter.setClickListener(View.OnClickListener {
            configWidget(MainListAdapter.itemId)
        })
    }

    private fun configWidget(id: Int) {
        val preferences = createPreferences(id)
        updateWidget(preferences)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun createPreferences(id: Int): SharedPreferences {
        val preferences =
            getSharedPreferences(WidgetProvider.WIDGET_PREFERENCES, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(WidgetProvider.TODO_LIST_ID + widgetID, id)
        editor.apply()
        return preferences
    }

    private fun updateWidget(preferences: SharedPreferences) {
        val widgetManager = AppWidgetManager.getInstance(applicationContext)
        WidgetProvider.updateAppWidget(applicationContext, widgetManager, preferences, widgetID)
    }
}