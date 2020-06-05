package io.github.mertturkmenoglu.vevericka.ui.password

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.login.LoginActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_password_reset.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask


class PasswordResetActivity : AppCompatActivity() {
    companion object {
        private const val KEY_EMAIL = "email"
        private const val TAG = "PasswordResetActivity"
        private const val INTENT_DELAY_MILLIS = 1000L
    }

    private lateinit var mEmailEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        mEmailEditText = passwordResetEmailTextInput.editText ?: throw IllegalStateException()

        passwordResetSendButton.setOnClickListener {
            val email = mEmailEditText.text?.toString()?.trim() ?: return@setOnClickListener

            if (email.isBlank()) {
                val text = getString(R.string.fill_empty_fields)
                Snackbar.make(it, text, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuthHelper.instance.sendPasswordResetEmail(email)
                .addOnSuccessListener { _ ->
                    val text = getString(R.string.password_reset_sent_ok_msg)
                    Snackbar.make(it, text, Snackbar.LENGTH_SHORT).show()
                    Handler().postDelayed({
                        startActivity(intentFor<LoginActivity>().clearTask().newTask())
                    }, INTENT_DELAY_MILLIS)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "passwordReset: ", e)
                    val text = getString(R.string.generic_err_msg)
                    Snackbar.make(it, text, Snackbar.LENGTH_SHORT).show()
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