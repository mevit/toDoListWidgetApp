package com.meldeveloping.todowidget.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.extension.showToast
import com.meldeveloping.todowidget.main.MainActivity
import kotlinx.android.synthetic.main.activity_widget_config.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WidgetConfigurationActivity : AppCompatActivity() {

    private val configViewModel: WidgetConfigViewModel by viewModel()
    private var widgetID = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var resultIntent: Intent
    private var selectedItem = 0
    private var selectedStyle = R.layout.widget_view_dark
    private var isItemSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        initWidgetID()
        initResultIntent()
        initNewButton()
        initWidgetConfigList()
        initChangeItemButton()
        initSelectColorButtons()
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
            initCreateButton()
        } else {
            configItemText.visibility = View.GONE
            configWidgetStyleText.visibility = View.GONE
            configRadioGroup.visibility = View.GONE
            configWidgetCreateButton.visibility = View.GONE
            configWidgetEmptyButton.visibility = View.VISIBLE
        }
    }

    private fun initList(adapter: MainListAdapter) {
        val layoutManager = LinearLayoutManager(applicationContext)
        configWidgetList.layoutManager = layoutManager
        configWidgetList.adapter = adapter
    }

    private fun initCreateButton() {
        configWidgetCreateButton.setOnClickListener {
            if(isItemSelected) {
                configWidget()
            } else {
                showToast(applicationContext, getString(R.string.config_toast_text))
            }

        }
    }

    private fun initChangeItemButton() {
        configItemChange.setOnClickListener {
            configWidgetList.visibility = View.VISIBLE
            configSelectedItemContainer.visibility = View.GONE
            configItemChange.visibility = View.GONE
            isItemSelected = false
        }
    }

    private fun initSelectColorButtons() {
        configStyleDark.setOnClickListener {
            changeBackgroundColor(configStyleDark, configStyleLight, configStylePurple)
            selectedStyle = R.layout.widget_view_dark
        }

        configStyleLight.setOnClickListener {
            changeBackgroundColor(configStyleLight, configStylePurple, configStyleDark)
            selectedStyle = R.layout.widget_view_light
        }

        configStylePurple.setOnClickListener {
            changeBackgroundColor(configStylePurple, configStyleDark, configStyleLight)
            selectedStyle = R.layout.widget_view_purple
        }
    }

    private fun changeBackgroundColor(mainView: TextView, secView: TextView, thrdView: TextView) {
        mainView.background = ContextCompat.getDrawable(applicationContext, R.drawable.img_main_item_bg_light)
        mainView.setTextColor(Color.parseColor("#33334d"))
        secView.background = ContextCompat.getDrawable(applicationContext, R.drawable.img_main_item_bg_dark)
        secView.setTextColor(Color.parseColor("#DCDCDC"))
        thrdView.background = ContextCompat.getDrawable(applicationContext, R.drawable.img_main_item_bg_dark)
        thrdView.setTextColor(Color.parseColor("#DCDCDC"))
    }

    private fun setAdapterListener(adapter: MainListAdapter) {
        adapter.setClickListener(View.OnClickListener {
            selectedItem = MainListAdapter.itemId
            selectItem()
            isItemSelected = true
        })
    }

    private fun configWidget() {
        val preferences = createPreferences()
        updateWidget(preferences)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun selectItem() {
        configWidgetList.visibility = View.GONE
        configSelectedItemContainer.visibility = View.VISIBLE
        configItemChange.visibility = View.VISIBLE
        configSelectedTitle.text = configViewModel.getItemTitle(selectedItem)
        configSelectedDate.text = configViewModel.getItemDate(selectedItem).dropLast(3)
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