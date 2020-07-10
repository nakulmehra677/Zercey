package com.zercey.paint.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.zercey.paint.R
import com.zercey.paint.managers.FirebaseAuthManager

class SplashActivity : AppCompatActivity() {

    private val TIME = 1000
    private lateinit var firebaseAuthManager: FirebaseAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash)
        firebaseAuthManager = FirebaseAuthManager()

        checkLogin()

    }

    private fun checkLogin() {


        Handler().postDelayed({
            if (firebaseAuthManager.userExist()) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()

        }, TIME.toLong())

    }
}