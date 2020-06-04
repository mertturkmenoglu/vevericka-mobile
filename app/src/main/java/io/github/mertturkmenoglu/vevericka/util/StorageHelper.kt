package io.github.mertturkmenoglu.vevericka.util

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

object StorageHelper {
    val instance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    val imagesRef: StorageReference by lazy { instance.reference.child("images") }

    fun uploadImageByteArray(childRef: String, image: ByteArray): UploadTask {
        return imagesRef.child(childRef).putBytes(image)
    }

    fun getProfilePictureUrl(uid: String): Task<Uri> = imagesRef.child(uid).downloadUrl

}