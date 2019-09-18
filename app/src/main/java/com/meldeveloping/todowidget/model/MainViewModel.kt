package com.meldeveloping.todowidget.model

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.repository.main.MainRepository

class MainViewModel(
    val repository: MainRepository
) : ViewModel() {
}