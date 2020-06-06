package io.github.mertturkmenoglu.vevericka.ui.verification

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.google.firebase.auth.FirebaseAuth
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.login.LoginActivity
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_verification.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar

class VerificationActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        private const val INTENT_DELAY_MILLIS = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        verificationSendButton.setOnClickListener(::sendEmail)
        verificationCheckButton.setOnClickListener(::checkVerification)
        verificationSignOutButton.setOnClickListener(::signOut)
    }

    private fun sendEmail(view: View) {
        FirebaseAuthHelper.sendVerificationEmail().addOnCompleteListener {
            if (it.isSuccessful) {
                view.snackbar(getString(R.string.verification_email_send_ok_msg))
            } else {
                error { "SendVerificationEmail failed: " + it.exception }
                view.snackbar(getString(R.string.verification_email_send_err_msg))
            }
        }
    }

    private fun checkVerification(view: View) {
        FirebaseAuth.getInstance().currentUser?.reload()?.addOnSuccessListener {
            if (FirebaseAuthHelper.isUserVerified()) {
                view.snackbar(getString(R.string.user_verified_ok_msg))
                Handler().postDelayed(INTENT_DELAY_MILLIS) {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                }
            } else {
                view.snackbar(getString(R.string.user_not_verified_msg))
            }
        }
    }

    private fun signOut(@Suppress("UNUSED_PARAMETER") view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(intentFor<LoginActivity>().newTask().clearTask())
    }
}