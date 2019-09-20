package com.meldeveloping.todowidget.model

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.repository.Repository

class EditViewModel(
    val repository: Repository
) : ViewModel() {
}
