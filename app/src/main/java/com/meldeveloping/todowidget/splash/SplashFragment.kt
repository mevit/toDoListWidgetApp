package com.meldeveloping.todowidget.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.meldeveloping.todowidget.R
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        splashScreenAnimation()
    }

    private fun splashScreenAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_splash_fragment)
        splashImageView.startAnimation(animation)
        animation.setAnimationListener( object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {}

            override fun onAnimationStart(p0: Animation?) {
                splashImageView.visibility = View.VISIBLE
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}
