package com.example.jello_projectmanag.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.adapters.TaskListItemsAdapter
import com.example.jello_projectmanag.databinding.ActivityTaskListBinding
import com.example.jello_projectmanag.firebase.FirestoreClass
import com.example.jello_projectmanag.models.Board
import com.example.jello_projectmanag.models.Card
import com.example.jello_projectmanag.models.Task
import com.example.jello_projectmanag.utils.Constants

class TaskListActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board
    private lateinit var binding: ActivityTaskListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var boardDocumentId = ""
        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }


        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this,boardDocumentId)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarTaskListActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.name
        }
        binding.toolbarTaskListActivity.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    fun boardDetails(board: Board){

        mBoardDetails = board
        hideProgressDialog()
        setupActionBar()


        val addTaskList = Task(resources.getString(R.string.add_list))

        board.taskList.add(addTaskList)

        binding.rvTaskList.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTaskList.setHasFixedSize(true)

        val adapter = TaskListItemsAdapter(this, board.taskList)
        binding.rvTaskList.adapter = adapter
    }

    fun addUpdateTaskListSuccess(){
        hideProgressDialog()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this,mBoardDetails.documentId)
    }

    fun createTaskList(taskListName: String){
        val task = Task(taskListName,FirestoreClass().getCurrentUserID())
        // Add a task to the list
        mBoardDetails.taskList.add(0,task)
        // Remove the last item from the list
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this,mBoardDetails)
    }

    fun updateTaskList(position: Int, listName: String, model: Task){
        val task = Task(listName,model.createdBy)
        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this,mBoardDetails)
    }

    fun deleteTaskList(position: Int){
        mBoardDetails.taskList.removeAt(position)

        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this,mBoardDetails)
    }

    fun addCardToTaskList(position: Int, cardName: String){
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        val cardAssignedUsersList: ArrayList<String> = ArrayList()
        cardAssignedUsersList.add(FirestoreClass().getCurrentUserID())

        val card = Card(cardName,FirestoreClass().getCurrentUserID(),cardAssignedUsersList)

        val cardsList = mBoardDetails.taskList[position].cards
        cardsList.add(card)

        val task = Task(mBoardDetails.taskList[position].title,
            mBoardDetails.taskList[position].createdBy,
            cardsList)

        mBoardDetails.taskList[position] = task

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this,mBoardDetails)
    }
}