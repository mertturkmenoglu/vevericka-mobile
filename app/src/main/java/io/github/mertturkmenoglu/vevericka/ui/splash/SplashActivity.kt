package io.github.mertturkmenoglu.vevericka.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gr.net.maroulis.library.EasySplashScreen
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.login.LoginActivity
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = EasySplashScreen(this)
            .withFullScreen()
            .withSplashTimeOut(100)
            .withBackgroundResource(android.R.color.white)
            .withLogo(R.drawable.ic_squirrel)
            .withTargetActivity(
                if (FirebaseAuthHelper.isLoggedIn) {
                    MainActivity::class.java
                } else {
                    LoginActivity::class.java
                }
            )

        setContentView(config.create())
    }
}