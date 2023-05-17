package com.example.jello_projectmanag.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.adapters.BoardItemsAdapter
import com.example.jello_projectmanag.databinding.ActivityMainBinding
import com.example.jello_projectmanag.databinding.NavHeaderMainBinding
import com.example.jello_projectmanag.firebase.FirestoreClass
import com.example.jello_projectmanag.models.Board
import com.example.jello_projectmanag.models.User
import com.example.jello_projectmanag.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var mUserName: String
    private lateinit var binding: ActivityMainBinding

    private val updateNavUserInfo = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            FirestoreClass().loadUserData(this)
        }else{
            println("DEBUG: Failed to update user info")
        }
    }

    private val updateBoardsList = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            FirestoreClass().getBoardsList(this)
        }else{
            println("DEBUG: Failed to update boards list")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()
        // Set the navigation view click listener
        binding.navView.setNavigationItemSelectedListener(this)

        FirestoreClass().loadUserData(this, true)

        binding.includedToolbar.fabCreateBoard.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            updateBoardsList.launch(intent)
        }
    }

    private fun setupActionBar() {
        val toolbarBinding = binding.includedToolbar.toolbarMainActivity
        setSupportActionBar(toolbarBinding)
        toolbarBinding.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbarBinding.setNavigationOnClickListener {
            // Toggle Drawer
            toggleDrawer()
        }

    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>){
        hideProgressDialog()
        val mainContentBinding = binding.includedToolbar.includedMainContent
        if (boardsList.size > 0) {

            mainContentBinding.rvBoardsList.visibility = View.VISIBLE
            mainContentBinding.tvNoBoardsAvailable.visibility = View.GONE

            mainContentBinding.rvBoardsList.layoutManager = LinearLayoutManager(this)
            mainContentBinding.rvBoardsList.setHasFixedSize(true)

            mainContentBinding.rvBoardsList.adapter = BoardItemsAdapter(this, boardsList)
        } else {
            mainContentBinding.rvBoardsList.visibility = View.GONE
            mainContentBinding.tvNoBoardsAvailable.visibility = View.VISIBLE
        }

    }
    private fun toggleDrawer() {
        // Check if the drawer is open then close it
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    // Back button functionality
    @Deprecated("Use doubleBackToExit() instead")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            doubleBackToExit()
        }
    }

    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {
        mUserName = user.name
        val headerView: View = binding.navView.getHeaderView(0)
        val headerBinding: NavHeaderMainBinding = NavHeaderMainBinding.bind(headerView)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_nav_user)
            .into(headerBinding.ivUserImage)

        headerBinding.tvUsername.text = user.name

        if (readBoardsList) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardsList(this)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                updateNavUserInfo.launch(Intent(this, MyProfileActivity::class.java))
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                // Clear the back stack and start new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}