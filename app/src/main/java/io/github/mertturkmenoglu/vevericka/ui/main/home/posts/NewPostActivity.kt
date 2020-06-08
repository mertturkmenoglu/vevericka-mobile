package io.github.mertturkmenoglu.vevericka.ui.main.home.posts

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.error
import java.io.ByteArrayOutputStream
import java.util.*

class NewPostActivity : AppCompatActivity(), AnkoLogger {
    private companion object {
        private const val RC_SELECT_IMAGE = 1
        private const val KEY_CONTENT = "content"
        private const val KEY_IMAGE = "image"
    }

    private lateinit var mContentEditText: EditText

    private lateinit var imageBytes: ByteArray
    private var isPictureSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.title = getString(R.string.title_activity_new_post)

        mContentEditText = newPostContentTextInput.editText ?: throw IllegalStateException()

        newPostImage.setOnClickListener {
            selectImage()
        }

        newPostAddNewPostFab.setOnClickListener {
            newPost(it)
        }
    }

    private fun newPost(view: View) {
        val content = mContentEditText.text?.toString()?.trim() ?: return

        if (!this::imageBytes.isInitialized) {
            view.snackbar(getString(R.string.select_image))
            return
        }

        if (content.isBlank()) {
            view.snackbar(getString(R.string.fill_empty_fields))
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val result = uploadImage(view, content)
            if (result) {
                finish()
            }
        }
    }

    private suspend fun uploadImage(view: View, content: String): Boolean {
        return try {
            val childRef = UUID.nameUUIDFromBytes(imageBytes).toString()
            val task = StorageHelper.uploadImageByteArray(childRef, imageBytes).await()
            val uri = task.storage.downloadUrl.await()
            val uid = FirebaseAuthHelper.getCurrentUserId()
            val post = Post(uid, content, uri.toString())
            uploadPost(post)
        } catch (e: Exception) {
            e.printStackTrace()
            view.snackbar(getString(R.string.generic_err_msg))
            false
        }
    }

    private suspend fun uploadPost(post: Post): Boolean {
        return try {
            val uid = FirebaseAuthHelper.getCurrentUserId()
            FirestoreHelper.addPost(uid, post).await()
            true
        } catch (e: Exception) {
            error { "UploadPost failed: $e" }
            e.printStackTrace()
            false
        }
    }

    private fun selectImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        }

        val title = getString(R.string.select_image)
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
            .into(newPostImage)
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

        outState.putString(KEY_CONTENT, mContentEditText.text?.toString())
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mContentEditText.setText(savedInstanceState.getString(KEY_CONTENT, ""))
        imageBytes = savedInstanceState.getByteArray(KEY_IMAGE) ?: return

        loadImageFromBytes()
    }
}