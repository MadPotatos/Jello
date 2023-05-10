package com.example.jello_projectmanag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.databinding.ActivityCreateBoardBinding


class CreateBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()
    }
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCreateBoardActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            actionBar.title = resources.getString(R.string.create_board_title)
        }
        binding.toolbarCreateBoardActivity.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

    }
}