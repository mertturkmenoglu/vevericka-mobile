package io.github.mertturkmenoglu.vevericka.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginLoginButton.setOnClickListener { view ->
            val emailEditText = loginEmailTextInput.editText ?: throw NoSuchElementException()
            val passwordEditText = loginPasswordTextInput.editText ?: throw NoSuchElementException()

            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            FirebaseAuthHelper.instance.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                }
                .addOnFailureListener {
                    Log.e(TAG, "onCreate: ", it)
                    Snackbar.make(view, "Cannot login", Snackbar.LENGTH_SHORT).show()
                }
        }

        loginHelp.setOnClickListener {
            LoginHelpDialog().show(supportFragmentManager, "Dialog")
        }
    }
}
