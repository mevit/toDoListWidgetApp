package com.meldeveloping.todowidget.help

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.main.fragments.MainFragment
import kotlinx.android.synthetic.main.fragment_help_config.*

class HelpConfigFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_help_config, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNextButton()
    }

    private fun initNextButton() {
        help_config_button.setOnClickListener {
            closeHelp()
            fragmentManager!!
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.mainContainer, MainFragment.newInstance())
                .commit()
        }
    }

    private fun closeHelp() {
        val preferences =
            context!!.getSharedPreferences(MainActivity.TODO_PREFERENCES, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(MainActivity.SHOW_HELP, false)
        editor.apply()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HelpConfigFragment()
    }
}
