package com.example.myrestaurentapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var runnableTask: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler(Looper.getMainLooper())
        runnableTask = Runnable { // This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this@SplashActivity, DashboardActivity::class.java)
            startActivity(i)

            // close this activity
            finish()
        }
        handler.postDelayed(runnableTask, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnableTask)
    }
}