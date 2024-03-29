package com.example.jello_projectmanag.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.databinding.ActivitySignInBinding
import com.example.jello_projectmanag.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
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

    // Sign in success function
    fun signInSuccess () {
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    //Set up the action bar
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSignInActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        }
        binding.toolbarSignInActivity.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnSignIn.setOnClickListener {
            signInRegisteredUser()
        }
    }

    //Sign in using email and password
    private fun signInRegisteredUser() {
        // Get the text from editText and trim the space
        val email: String = binding.etEmailSignin.text.toString().trim { it <= ' ' }
        val password: String = binding.etPasswordSignin.text.toString()
        if (validateForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            // Sign in using FirebaseAuth
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        // Sign in success
                      FirestoreClass().loadUserData(this)

                    } else {
                        // Sign in failed
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    // Validate
    private fun validateForm(email: String, password: String): Boolean {
        return when {
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