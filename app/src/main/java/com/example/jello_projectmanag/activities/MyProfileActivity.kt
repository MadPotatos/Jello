package com.example.jello_projectmanag.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.databinding.ActivityMyProfileBinding
import com.example.jello_projectmanag.firebase.FirestoreClass
import com.example.jello_projectmanag.models.User


class MyProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()
        FirestoreClass().loadUserData(this)
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
            .into(binding.ivUserImage)

        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)
        if (user.mobile != 0L) {
            binding.etMobile.setText(user.mobile.toString())
        }
    }
}
