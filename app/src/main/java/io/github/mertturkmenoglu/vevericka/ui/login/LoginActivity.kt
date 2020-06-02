package io.github.mertturkmenoglu.vevericka.ui.login

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.ui.register.RegisterActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

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

            if (email.isBlank() || password.isBlank()) {
                Snackbar.make(view, "Empty field", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuthHelper.instance.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                }
                .addOnFailureListener {
                    Log.e(TAG, "onCreate: ", it)
                    val message = it.message ?: getString(R.string.login_err_msg)
                    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
                }
        }

        loginHelpButton.setOnClickListener {
            helpDialog(it)
        }

        loginHelpText.setOnClickListener {
            helpDialog(it)
        }
    }

    private fun helpDialog(view: View) {
        val selections = listOf(
            getString(R.string.forget_password),
            getString(R.string.sign_up_text)
        )

        selector(getString(R.string.help), selections) { _, i ->
            when (i) {
                0 -> passwordReset(view)
                1 -> startActivity(intentFor<RegisterActivity>())
            }
        }
    }

    private fun passwordReset(view: View) {
        alert {
            title = getString(R.string.send_password_reset_link)

            customView {
                val input = editText {
                    hint = getString(R.string.email)
                    singleLine = true
                    inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }

                positiveButton(getString(R.string.send)) {
                    val email = input.text?.toString()?.trim() ?: throw NoSuchElementException()

                    FirebaseAuthHelper.instance.sendPasswordResetEmail(email)
                        .addOnSuccessListener {
                            Snackbar.make(
                                view,
                                getString(R.string.password_reset_sent_ok_msg),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "passwordReset: ", e)
                            Snackbar.make(view,
                                getString(R.string.generic_err_msg), Snackbar.LENGTH_SHORT).show()
                        }
                }
            }
        }.show()
    }
}
