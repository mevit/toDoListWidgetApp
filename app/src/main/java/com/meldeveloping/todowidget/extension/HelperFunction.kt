package com.meldeveloping.todowidget.extension

fun Int.toBoolean() = this != 0

fun Boolean.toInt() = if (this) 1 else 0

//Log.i("TAG", "----------------------------------")
