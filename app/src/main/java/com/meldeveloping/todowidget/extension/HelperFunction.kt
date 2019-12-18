package com.meldeveloping.todowidget.extension

import android.content.Context
import android.util.Log
import android.widget.Toast
import android.R.attr.bottom
import android.opengl.ETC1.getHeight
import android.app.Activity
import android.graphics.Rect
import android.view.View


fun Int.toBoolean() = this != 0

fun Boolean.toInt() = if (this) 1 else 0

fun showLog(message: String = "-------------------------") {
    Log.i("myLog", message)
}

fun showToast(context: Context, message: String = "message") {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun isKeyboardVisible(activity: Activity): Boolean {
    val r = Rect()
    val contentView = activity.findViewById<View>(android.R.id.content)
    contentView.getWindowVisibleDisplayFrame(r)
    val screenHeight = contentView.rootView.height

    val keypadHeight = screenHeight - r.bottom

    return keypadHeight > screenHeight * 0.15
}
