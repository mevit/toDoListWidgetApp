package com.meldeveloping.todowidget.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.main.MainActivity
import kotlinx.android.synthetic.main.activity_widget_config.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WidgetConfigurationActivity : AppCompatActivity() {

    private val configViewModel: WidgetConfigViewModel by viewModel()
    private var widgetID = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var resultIntent: Intent
    private var selectedItem = 0
    private var selectedStyle = 0

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
            selectStyle()
            initCreateButton()
        } else {
            configWidgetEmptyButton.visibility = View.VISIBLE
        }
    }

    private fun selectStyle() {
        configWidgetDarkButton.setOnClickListener{
            configWidgetPreview.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.img_main_frg_empty_dark
                )
            )
            selectedStyle = R.layout.widget_view_dark
        }
        configWidgetLightButton.setOnClickListener {
            configWidgetPreview.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.img_main_frg_empty_light
                )
            )
            selectedStyle = R.layout.widget_view_light
        }
        configWidgetDarkButton.callOnClick()
    }

    private fun initList(adapter: MainListAdapter) {
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        configWidgetList.layoutManager = layoutManager
        configWidgetList.adapter = adapter
    }

    private fun initCreateButton() {
        configWidgetCreateButton.setOnClickListener {
            configWidget()
        }
    }

    private fun setAdapterListener(adapter: MainListAdapter) {
        adapter.setClickListener(View.OnClickListener {
//            configWidget(MainListAdapter.itemId)
            selectedItem = MainListAdapter.itemId
        })
    }

    private fun configWidget() {
        val preferences = createPreferences()
        updateWidget(preferences)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun createPreferences(): SharedPreferences {
        val preferences =
            getSharedPreferences(WidgetProvider.WIDGET_PREFERENCES, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(WidgetProvider.TODO_LIST_ID + widgetID, selectedItem)
        editor.putInt(WidgetProvider.TODO_LIST_STYLE + widgetID, selectedStyle)
        editor.apply()
        return preferences
    }

    private fun updateWidget(preferences: SharedPreferences) {
        val widgetManager = AppWidgetManager.getInstance(applicationContext)
        WidgetProvider.updateAppWidget(applicationContext, widgetManager, preferences, widgetID)
    }
}