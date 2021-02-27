package com.kev95p.pruebakotlin.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.kev95p.pruebakotlin.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed(object : Runnable {
            override fun run() {
                val shared = applicationContext.getSharedPreferences("pref", 0)
                val userKey: String? = shared.getString("currentUser", null)
                if (userKey != null) {
                    val newIntent = Intent(applicationContext, MainActivity::class.java)
                    newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(newIntent)
                } else {
                    val newIntent = Intent(applicationContext, LoginActivity::class.java)
                    newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(newIntent)
                }
            }

        }, 1000)
    }
}