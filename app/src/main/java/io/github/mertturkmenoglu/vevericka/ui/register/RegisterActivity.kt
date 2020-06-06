package io.github.mertturkmenoglu.vevericka.ui.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.User
import io.github.mertturkmenoglu.vevericka.ui.main.MainActivity
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import java.io.ByteArrayOutputStream


class RegisterActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        private const val RC_SELECT_IMAGE = 1
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_IMAGE = "image"
    }

    private lateinit var mFirstNameEditText: EditText
    private lateinit var mLastNameEditText: EditText
    private lateinit var mEmailEditText: EditText
    private lateinit var mPasswordEditText: EditText

    private lateinit var imageBytes: ByteArray
    private var isPictureSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        resetViews()

        mFirstNameEditText = registerFirstNameTextInput.editText ?: throw IllegalStateException()
        mLastNameEditText = registerLastNameTextInput.editText ?: throw IllegalStateException()
        mEmailEditText = registerEmailTextInput.editText ?: throw IllegalStateException()
        mPasswordEditText = registerPasswordTextInput.editText ?: throw IllegalStateException()

        registerProfileImage.setOnClickListener {
            selectProfileImage()
        }

        registerRegisterButton.setOnClickListener {
            registerClick(it)
        }
    }

    private fun resetViews() {
        registerProgressBar.visibility = GONE
        registerRegisterButton.isClickable = true
    }

    private fun prepareViews() {
        registerProgressBar.visibility = VISIBLE
        registerRegisterButton.isClickable = false
    }

    private fun registerClick(view: View) {
        val firstName = mFirstNameEditText.text?.toString()?.trim() ?: return
        val lastName = mLastNameEditText.text?.toString()?.trim() ?: return
        val email = mEmailEditText.text?.toString()?.trim() ?: return
        val password = mPasswordEditText.text?.toString()?.trim() ?: return

        if (listOf(firstName, lastName, email, password).any { it.isBlank() }) {
            view.snackbar(getString(R.string.fill_empty_fields))
            return
        }

        val user = User(firstName, lastName, email)

        prepareViews()

        FirebaseAuthHelper.createUser(email, password)
            .addOnSuccessListener { onCreateUserSuccess(view, user, password) }
            .addOnFailureListener { onCreateUserFail(view, it) }
    }

    private fun onCreateUserSuccess(view: View, user: User, password: String) {
        view.snackbar(getString(R.string.user_create_ok_msg))
        signIn(view, user, password)
    }

    private fun signIn(view: View, user: User, password: String) {
        FirebaseAuthHelper.signIn(user.email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val uid = it.result?.user?.uid ?: System.currentTimeMillis().toString()
                onSignInSuccess(user, uid)
            } else {
                error { "SignIn failed: " + it.exception }
                resetViews()
                view.snackbar(it.exception?.localizedMessage ?: getString(R.string.generic_err_msg))
            }
        }
    }

    private fun onCreateUserFail(view: View, e: Exception) {
        error { "UserCreate failed: $e" }
        resetViews()
        view.snackbar(e.localizedMessage ?: getString(R.string.generic_err_msg))
    }

    private fun onSignInSuccess(user: User, uid: String) {
        FirestoreHelper.saveUser(user).addOnCompleteListener {
            if (!it.isSuccessful) {
                error { "UserSave failed: " + it.exception }
                resetViews()
            } else {
                onDatabaseSaveSuccess(uid)
            }
        }
    }

    private fun onDatabaseSaveSuccess(uid: String) {
        if (!this::imageBytes.isInitialized) {
            startActivity(intentFor<MainActivity>().clearTask().newTask())
        } else {
            uploadImage(uid)
        }
    }

    private fun uploadImage(uid: String) {
        StorageHelper.uploadImageByteArray(uid, imageBytes).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(intentFor<MainActivity>().clearTask().newTask())
            } else {
                error { "ImageUpload failed: " + it.exception }
                resetViews()
            }
        }
    }

    private fun selectProfileImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        }

        val title = getString(R.string.select_profile_picture)
        startActivityForResult(Intent.createChooser(intent, title), RC_SELECT_IMAGE)
    }

    private fun setImageBytesFromUri(uri: Uri?) {
        val selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val outputStream = ByteArrayOutputStream()
        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        imageBytes = outputStream.toByteArray()
        isPictureSelected = true
    }

    private fun loadImageFromBytes() {
        Glide.with(this)
            .load(imageBytes)
            .override(125, 125)
            .apply(RequestOptions().circleCrop())
            .into(registerProfileImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            setImageBytesFromUri(data.data)
            loadImageFromBytes()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (this::imageBytes.isInitialized) {
            outState.putByteArray(KEY_IMAGE, imageBytes)
        }

        outState.putString(KEY_FIRST_NAME, mFirstNameEditText.text?.toString())
        outState.putString(KEY_LAST_NAME, mLastNameEditText.text?.toString())
        outState.putString(KEY_EMAIL, mEmailEditText.text?.toString())
        outState.putString(KEY_PASSWORD, mPasswordEditText.text?.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mFirstNameEditText.setText(savedInstanceState.getString(KEY_FIRST_NAME, ""))
        mLastNameEditText.setText(savedInstanceState.getString(KEY_LAST_NAME, ""))
        mEmailEditText.setText(savedInstanceState.getString(KEY_EMAIL, ""))
        mPasswordEditText.setText(savedInstanceState.getString(KEY_PASSWORD, ""))

        imageBytes = savedInstanceState.getByteArray(KEY_IMAGE) ?: return
        loadImageFromBytes()
    }
}