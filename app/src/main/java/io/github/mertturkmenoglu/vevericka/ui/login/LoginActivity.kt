package io.github.mertturkmenoglu.vevericka.ui.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.ui.password.PasswordResetActivity
import io.github.mertturkmenoglu.vevericka.ui.register.RegisterActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar

class LoginActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
    }

    private lateinit var mEmailEditText: EditText
    private lateinit var mPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mEmailEditText = loginEmailTextInput.editText ?: throw IllegalStateException()
        mPasswordEditText = loginPasswordTextInput.editText ?: throw IllegalStateException()

        loginLoginButton.setOnClickListener { onLoginClick(it) }
        loginHelpButton.setOnClickListener { helpDialog() }
        loginHelpText.setOnClickListener { helpDialog() }
    }

    private fun onLoginClick(view: View) {
        val email = mEmailEditText.text?.toString()?.trim() ?: return
        val password = mPasswordEditText.text?.toString()?.trim() ?: return

        if (listOf(email, password).any { it.isBlank() }) {
            val text = getString(R.string.login_empty_field)
            view.snackbar(text)
            return
        }

        signIn(view, email, password)
    }

    private fun signIn(view: View, email: String, password: String) {
        FirebaseAuthHelper.signIn(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(intentFor<MainActivity>().newTask().clearTask())
            } else {
                val message = it.exception?.localizedMessage ?: getString(R.string.login_err_msg)
                error("Sign-in failure: ", it.exception)
                view.snackbar(message)
            }
        }
    }

    private fun helpDialog() {
        val selections = listOf(
            getString(R.string.forget_password),
            getString(R.string.sign_up_text)
        )

        selector(getString(R.string.help), selections) { _, i ->
            when (i) {
                0 -> startActivity(intentFor<PasswordResetActivity>())
                1 -> startActivity(intentFor<RegisterActivity>())
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_EMAIL, mEmailEditText.text?.toString())
        outState.putString(KEY_PASSWORD, mPasswordEditText.text?.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mEmailEditText.setText(savedInstanceState.getString(KEY_EMAIL, ""))
        mPasswordEditText.setText(savedInstanceState.getString(KEY_PASSWORD, ""))
    }
}
