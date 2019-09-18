package com.meldeveloping.todowidget.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.main.fragments.MainFragment
import com.meldeveloping.todowidget.splash.SplashFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_CHANGE_DELAY_MS = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, SplashFragment.newInstance())
            .commit()

        Handler().postDelayed({
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, MainFragment.newInstance())
                .commit()
        }, FRAGMENT_CHANGE_DELAY_MS)

    }
}
