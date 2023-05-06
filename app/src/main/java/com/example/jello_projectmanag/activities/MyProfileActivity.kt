package com.example.jello_projectmanag.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.databinding.ActivityMyProfileBinding
import com.example.jello_projectmanag.firebase.FirestoreClass
import com.example.jello_projectmanag.models.User


class MyProfileActivity : BaseActivity() {

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        //private const val PICK_IMAGE_REQUEST_CODE = 2
    }
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var binding: ActivityMyProfileBinding

    //
    private val getImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK && it.data != null){
            mSelectedImageFileUri = it.data!!.data
            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_nav_user)
                    .into(binding.ivProfileUserImage)
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()
        FirestoreClass().loadUserData(this)

        binding.ivProfileUserImage.setOnClickListener {
            // If the permission is already allowed or we need to request for it.
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == android.content.pm.PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }
            else{
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if( requestCode == READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty()
                && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED){
                    showImageChooser()
            }
        }else{
                Toast.makeText(this, "Oops, you just denied the permission for storage. You can allow it from the settings.",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun showImageChooser(){
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // Launches the image selection of phone storage using the constant code.
        getImage.launch(galleryIntent)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarMyProfileActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }
        binding.toolbarMyProfileActivity.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    fun setUserDataInUI(user: User) {
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_nav_user)
            .into(binding.ivProfileUserImage)

        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)
        if (user.mobile != 0L) {
            binding.etMobile.setText(user.mobile.toString())
        }
    }
}
