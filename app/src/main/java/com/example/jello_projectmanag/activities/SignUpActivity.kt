package com.example.jello_projectmanag.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        // Hide the status bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

    }
    //Set up the action bar
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSignUpActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        }
        binding.toolbarSignUpActivity.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.btnSignUp.setOnClickListener {
            registerUser()
        }
    }


    private fun registerUser(){
        // Get the text from editText and trim the space
        val name: String = binding.etName.text.toString().trim{ it <= ' '}
        val email: String = binding.etEmail.text.toString().trim{ it <= ' '}
        val password: String = binding.etPassword.text.toString()

        if(validateForm(name, email, password)){
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
           TextUtils.isEmpty(name) -> {
               showErrorSnackBar(resources.getString(R.string.err_msg_enter_name))
               false
           }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email))
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password))
                false
            }
            else -> {
                true
            }
        }
    }
}