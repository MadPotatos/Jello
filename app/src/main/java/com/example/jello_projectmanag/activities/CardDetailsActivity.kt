package com.example.jello_projectmanag.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.databinding.ActivityCardDetailsBinding
import com.example.jello_projectmanag.models.Board
import com.example.jello_projectmanag.utils.Constants

class CardDetailsActivity : AppCompatActivity() {

    private lateinit var mBoardDetails: Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1


    private lateinit var binding: ActivityCardDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getIntentData()
        setupActionBar()

        binding.etNameCardDetails.setText(mBoardDetails
            .taskList[mTaskListPosition]
            .cards[mCardPosition].name)

        //Set the cursor to the end of the existing text
        binding.etNameCardDetails
            .setSelection(binding
                .etNameCardDetails.text.toString().length)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCardDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name

        }
        binding.toolbarCardDetailsActivity.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun getIntentData(){
        if(intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                intent.getParcelableExtra(Constants.BOARD_DETAIL, Board::class.java)!!

            } else{
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
            }
        }
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
    }
}