package com.meldeveloping.todowidget.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.model.EditViewModel
import org.koin.android.ext.android.inject

class EditFragment : Fragment() {

    private val editViewModel: EditViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit, container, false)

    companion object {

        @JvmStatic
        fun newInstance() = EditFragment()
    }

}
