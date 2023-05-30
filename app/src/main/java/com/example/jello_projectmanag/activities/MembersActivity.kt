package com.example.jello_projectmanag.activities

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.adapters.MemberListItemsAdapter
import com.example.jello_projectmanag.databinding.ActivityMembersBinding
import com.example.jello_projectmanag.firebase.FirestoreClass
import com.example.jello_projectmanag.models.Board
import com.example.jello_projectmanag.models.User
import com.example.jello_projectmanag.utils.Constants

class MembersActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board
    private lateinit var binding: ActivityMembersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = if(VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                intent.getParcelableExtra(Constants.BOARD_DETAIL, Board::class.java)!!

            } else{
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
            }
        }

        setupActionBar()

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }

    fun setupMembersList(list: ArrayList<User>){
       hideProgressDialog()

        binding.rvMembersList.layoutManager = LinearLayoutManager(this)
        binding.rvMembersList.setHasFixedSize(true)

        val adapter = MemberListItemsAdapter(this, list)
        binding.rvMembersList.adapter = adapter
    }
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarMembersActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }
        binding.toolbarMembersActivity.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}