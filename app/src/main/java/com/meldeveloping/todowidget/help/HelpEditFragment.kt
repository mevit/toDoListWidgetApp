package com.meldeveloping.todowidget.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.meldeveloping.todowidget.R
import kotlinx.android.synthetic.main.fragment_help_edit.*

class HelpEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_help_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNextButton()
    }

    private fun initNextButton() {
        help_edit_button.setOnClickListener {
            fragmentManager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.mainContainer, HelpWidgetFragment.newInstance())
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HelpEditFragment()
    }
}
