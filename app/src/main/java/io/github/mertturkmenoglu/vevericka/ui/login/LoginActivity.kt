package io.github.mertturkmenoglu.vevericka.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.ui.password.PasswordResetActivity
import io.github.mertturkmenoglu.vevericka.ui.register.RegisterActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.selector

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "LoginActivity"
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

        loginLoginButton.setOnClickListener { view ->
            val email = mEmailEditText.text?.toString()?.trim() ?: return@setOnClickListener
            val password = mPasswordEditText.text?.toString()?.trim() ?: return@setOnClickListener

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

        loginHelpButton.setOnClickListener { helpDialog() }
        loginHelpText.setOnClickListener { helpDialog() }
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
