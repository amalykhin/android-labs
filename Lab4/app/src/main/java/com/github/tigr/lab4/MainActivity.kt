package com.github.tigr.lab4

import android.graphics.drawable.AnimationDrawable
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView.setBackgroundResource(R.drawable.animation)

        button.setOnClickListener {
            val animation= imageView.background as AnimationDrawable
            val timer = object : CountDownTimer(10000, 16) {
                override fun onTick(left: Long) {
                    progressBar.progress = 10000 - left.toInt()
                }

                override fun onFinish() {
                    progressBar.progress = 0
                    animation.stop()
                    button.isEnabled = true
                }
            }

            if(animation.isRunning) {
                animation.stop()
                timer.cancel()
                button.isEnabled = true
            } else {
                animation.start()
                timer.start()
                button.isEnabled = false
            }

        }
    }
}
