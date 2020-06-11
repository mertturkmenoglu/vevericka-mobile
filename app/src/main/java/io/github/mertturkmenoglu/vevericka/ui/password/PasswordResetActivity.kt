package io.github.mertturkmenoglu.vevericka.ui.password

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.login.LoginActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_password_reset.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar


class PasswordResetActivity : AppCompatActivity(), AnkoLogger {
    private companion object {
        private const val KEY_EMAIL = "email"
        private const val INTENT_DELAY_MILLIS = 1000L
    }

    private lateinit var mEmailEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        mEmailEditText = passwordResetEmailTextInput.editText ?: throw IllegalStateException()

        passwordResetSendButton.setOnClickListener(::onResetButtonClick)
    }

    private fun onResetButtonClick(view: View) {
        val email = mEmailEditText.text?.toString()?.trim() ?: return

        if (email.isBlank()) {
            view.snackbar(getString(R.string.fill_empty_fields))
            return
        }

        sendEmail(view, email)
    }

    private fun sendEmail(view: View, email: String) {
        FirebaseAuthHelper.instance.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                view.snackbar(getString(R.string.password_reset_sent_ok_msg))
                Handler().postDelayed(INTENT_DELAY_MILLIS) {
                    startActivity(intentFor<LoginActivity>().clearTask().newTask())
                }
            } else {
                error { "SendPasswordResetEmail failed: " + it.exception }
                view.snackbar(getString(R.string.generic_err_msg))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_EMAIL, mEmailEditText.text?.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mEmailEditText.setText(savedInstanceState.getString(KEY_EMAIL, ""))
    }
}