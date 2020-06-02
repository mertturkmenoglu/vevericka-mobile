package io.github.mertturkmenoglu.vevericka.ui.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import java.io.ByteArrayOutputStream

private const val TAG = "RegisterActivity"
private const val RC_SELECT_IMAGE = 1
private const val KEY_EMAIL = "email"
private const val KEY_PASSWORD = "password"
private const val KEY_IMAGE = "image"

class RegisterActivity : AppCompatActivity() {
    private lateinit var imageBytes: ByteArray
    private var isPictureSelected = false

    private lateinit var mEmailEditText: EditText
    private lateinit var mPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mEmailEditText = registerEmailTextInput.editText ?: throw IllegalStateException()
        mPasswordEditText = registerPasswordTextInput.editText ?: throw IllegalStateException()

        registerProfileImage.setOnClickListener {
            profileImageClick()
        }

        registerRegisterButton.setOnClickListener {
            registerClick(it)
        }
    }

    private fun registerClick(view: View) {
        val email = mEmailEditText.text?.toString()?.trim() ?: return
        val password = mPasswordEditText.text?.toString()?.trim() ?: return

        if (email.isBlank() || password.isBlank()) {
            val text = getString(R.string.fill_empty_fields)
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
            return
        }

        val userTask = FirebaseAuthHelper.instance.createUserWithEmailAndPassword(email, password)

        userTask.addOnSuccessListener {
            val okMessage = getString(R.string.user_create_ok_msg)
            Snackbar.make(view, okMessage, Snackbar.LENGTH_SHORT).show()

            FirebaseAuthHelper.instance.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // TODO: Add DB and Storage operations
                    startActivity(intentFor<MainActivity>().clearTask().newTask())
                }
                .addOnFailureListener {
                    Log.e(TAG, "registerClick: ", it)
                    val message = it.localizedMessage ?: getString(R.string.generic_err_msg)
                    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
                }
        }

        userTask.addOnFailureListener {
            Log.e(TAG, "registerClick: ", it)
            val message = it.localizedMessage ?: getString(R.string.generic_err_msg)
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun profileImageClick() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        }

        val title = getString(R.string.select_profile_picture)
        startActivityForResult(Intent.createChooser(intent, title), RC_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null
            && data.data != null
        ) {
            val imagePath = data.data
            val selectedBmp = MediaStore.Images.Media.getBitmap(contentResolver, imagePath)

            val outputStream = ByteArrayOutputStream()
            selectedBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            imageBytes = outputStream.toByteArray()

            Glide.with(this)
                .load(imageBytes)
                .override(125, 125)
                .apply(RequestOptions().circleCrop())
                .into(registerProfileImage)

            isPictureSelected = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (this::imageBytes.isInitialized) {
            outState.putByteArray(KEY_IMAGE, imageBytes)
        }

        outState.putString(KEY_EMAIL, mEmailEditText.text?.toString())
        outState.putString(KEY_PASSWORD, mPasswordEditText.text?.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val email = savedInstanceState.getString(KEY_EMAIL, "")
        val password = savedInstanceState.getString(KEY_PASSWORD, "")

        mEmailEditText.setText(email)
        mPasswordEditText.setText(password)

        imageBytes = savedInstanceState.getByteArray(KEY_IMAGE) ?: return

        Glide.with(this)
            .load(imageBytes)
            .apply(RequestOptions().circleCrop())
            .override(125, 125)
            .into(registerProfileImage)
    }
}