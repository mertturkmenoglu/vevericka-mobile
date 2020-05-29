package io.github.mertturkmenoglu.vevericka.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import io.github.mertturkmenoglu.vevericka.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginLoginButton.setOnClickListener {
            Snackbar.make(it, "Hi", Snackbar.LENGTH_SHORT).show()
        }

        loginHelp.setOnClickListener {
            LoginHelpDialog().show(supportFragmentManager, "Dialog")
        }
    }

}
