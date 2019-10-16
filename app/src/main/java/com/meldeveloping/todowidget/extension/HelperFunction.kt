package com.meldeveloping.todowidget.extension

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Int.toBoolean() = this != 0

fun Boolean.toInt() = if (this) 1 else 0

fun showLog(message: String = "-------------------------") {
    Log.i("log", message)
}

fun showToast(context: Context, message: String = "message") {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
