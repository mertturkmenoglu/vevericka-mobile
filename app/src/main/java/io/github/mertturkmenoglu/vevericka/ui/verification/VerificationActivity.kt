package io.github.mertturkmenoglu.vevericka.ui.verification

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.login.LoginActivity
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_verification.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class VerificationActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "VerificationActivity"
        private const val INTENT_DELAY_MILLIS = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        verificationSendButton.setOnClickListener { view ->
            FirebaseAuthHelper.sendVerificationEmail()
                .addOnSuccessListener {
                    val text = getString(R.string.verification_email_send_ok_msg)
                    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "onCreate: ", e)
                    val text = getString(R.string.verification_email_send_err_msg)
                    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
                }
        }

        verificationCheckButton.setOnClickListener { view ->
            FirebaseAuth.getInstance().currentUser?.reload()?.addOnSuccessListener {
                if (FirebaseAuthHelper.isUserVerified()) {
                    val text = getString(R.string.user_verified_ok_msg)
                    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
                    Handler().postDelayed({
                        startActivity(intentFor<MainActivity>().newTask().clearTask())
                    }, INTENT_DELAY_MILLIS)
                } else {
                    val text = getString(R.string.user_not_verified_msg)
                    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
                }

            }
        }

        verificationSignOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(intentFor<LoginActivity>().newTask().clearTask())
        }
    }
}