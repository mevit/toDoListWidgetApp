package com.meldeveloping.todowidget.model

import androidx.lifecycle.ViewModel
import com.meldeveloping.todowidget.repository.edit.EditRepository

class EditViewModel(
    val repository: EditRepository
) : ViewModel() {
}
