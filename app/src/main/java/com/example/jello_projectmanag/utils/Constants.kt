package com.example.jello_projectmanag.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.jello_projectmanag.activities.CreateBoardActivity
import com.example.jello_projectmanag.activities.MyProfileActivity

object Constants {

    const val USERS: String = "users"
    const val BOARDS: String = "boards"
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"
    const val DOCUMENT_ID: String = "documentId"
    const val READ_STORAGE_PERMISSION_CODE = 1

    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // Launches the image selection of phone storage using the constant code.
        when (activity) {
            is MyProfileActivity -> {
                activity.getProfileImage.launch(galleryIntent)
            }
            is CreateBoardActivity -> {
                activity.getBoardImage.launch(galleryIntent)
            }
        }
    }

    //  Get the file extension of a file referenced by a Uri
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}
