package com.meldeveloping.todowidget.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meldeveloping.todowidget.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNewButton()
    }

    private fun initNewButton(){
        goToEditButton.setOnClickListener {
            goToEditFragment()
        }
    }

    private fun goToEditFragment(){
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.mainContainer, EditFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

}
