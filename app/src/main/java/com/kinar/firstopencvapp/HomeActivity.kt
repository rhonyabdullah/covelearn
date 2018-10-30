package com.kinar.firstopencvapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kinar.firstopencvapp.extensions.start
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bMean.setOnClickListener {
            start(MainActivity::class.java) {
                putExtra(ACTION_MODE, MEAN_BLUR)
            }
        }
    }

    companion object {
        const val ACTION_MODE = "HomeActivity.ACTION_MODE"
        const val MEAN_BLUR = 1
    }
}
