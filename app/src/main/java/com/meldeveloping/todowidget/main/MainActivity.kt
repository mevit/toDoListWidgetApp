package com.meldeveloping.todowidget.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.help.HelpMainFragment
import com.meldeveloping.todowidget.main.fragments.EditFragment
import com.meldeveloping.todowidget.main.fragments.MainFragment
import com.meldeveloping.todowidget.splash.SplashFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_CHANGE_DELAY_MS = 2000L
        private const val AD_UNIT_ID = "[ADMOB_UNIT_ID]"
        const val OPEN_EDIT_FRAGMENT = "open_edit_fragment"
        const val DEFAULT_TODO_LIST_ID = -1
        const val TODO_PREFERENCES = "preferences"
        const val SHOW_HELP = "show_help"
        var showAdFlag = false
        lateinit var mInterstitialAd: InterstitialAd
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = AD_UNIT_ID
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        val extras = intent.extras
        if (extras != null && extras.getInt(
                OPEN_EDIT_FRAGMENT,
                DEFAULT_TODO_LIST_ID
            ) != DEFAULT_TODO_LIST_ID
        ) {
            openEditFragment(extras.getInt(OPEN_EDIT_FRAGMENT))
        } else {
            openSplashFragment()
            if (checkShowHelp()) {
                openHelpMainFragment()
            } else {
                openMainFragment()
            }
        }
    }

    private fun checkShowHelp(): Boolean {
        val preferences = applicationContext.getSharedPreferences(TODO_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getBoolean(SHOW_HELP, true)
    }

    private fun openHelpMainFragment() {
        Handler().postDelayed({
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.mainContainer, HelpMainFragment.newInstance())
                .commit()
        }, FRAGMENT_CHANGE_DELAY_MS)
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
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
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
