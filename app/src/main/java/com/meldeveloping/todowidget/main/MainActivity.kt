package com.meldeveloping.todowidget.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.main.fragments.EditFragment
import com.meldeveloping.todowidget.main.fragments.MainFragment
import com.meldeveloping.todowidget.splash.SplashFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_CHANGE_DELAY_MS = 2000L
        const val OPEN_EDIT_FRAGMENT = "open_edit_fragment"
        const val DEFAULT_TODO_LIST_ID = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extras = intent.extras

        if (extras != null && extras.getInt(OPEN_EDIT_FRAGMENT, DEFAULT_TODO_LIST_ID) != DEFAULT_TODO_LIST_ID) {
            openEditFragment(extras.getInt(OPEN_EDIT_FRAGMENT))
        } else {
            openSplashFragment()
            openMainFragment()
        }
    }

    private fun openSplashFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, SplashFragment.newInstance())
            .commit()
    }

    private fun openMainFragment() {
        Handler().postDelayed({
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, MainFragment.newInstance())
                .commit()
        }, FRAGMENT_CHANGE_DELAY_MS)
    }

    private fun openEditFragment(toDoListId: Int) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.mainContainer, EditFragment.newInstance(toDoListId))
                .commit()
    }

}
