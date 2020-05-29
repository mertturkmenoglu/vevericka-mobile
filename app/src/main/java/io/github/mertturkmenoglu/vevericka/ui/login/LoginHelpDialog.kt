package io.github.mertturkmenoglu.vevericka.ui.login

import android.app.Dialog
import android.os.Bundle
import android.view.InflateException
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import kotlinx.android.synthetic.main.dialog_login_help.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast

class LoginHelpDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity ?: throw Exception())
        val inflater = activity?.layoutInflater ?: throw InflateException()
        val view = inflater.inflate(R.layout.dialog_login_help, null)

        val forgetPassword: TextView = view.loginForgetPassword
        val signUp: TextView = view.loginSignUp

        forgetPassword.setOnClickListener {
            toast("Forget password")
            startActivity(intentFor<MainActivity>().clearTask().newTask())
        }

        signUp.setOnClickListener {
            toast("Sign up")
            startActivity(intentFor<MainActivity>().clearTask().newTask())
        }

        return builder.setView(view).create()
    }

}