package com.example.jello_projectmanag.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.jello_projectmanag.activities.CardDetailsActivity
import com.example.jello_projectmanag.activities.CreateBoardActivity
import com.example.jello_projectmanag.activities.MainActivity
import com.example.jello_projectmanag.activities.MembersActivity
import com.example.jello_projectmanag.activities.MyProfileActivity
import com.example.jello_projectmanag.activities.SignUpActivity
import com.example.jello_projectmanag.activities.SignInActivity
import com.example.jello_projectmanag.activities.TaskListActivity
import com.example.jello_projectmanag.models.Board
import com.example.jello_projectmanag.models.User
import com.example.jello_projectmanag.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FirestoreClass {

    // Create a instance of Firebase Firestore
    private val mFireStore = Firebase.firestore


    fun registerUser(activity: SignUpActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {


                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    fun createBoard(activity: CreateBoardActivity, board: Board) {

        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Board created successfully!")
                Toast.makeText(activity, "Board created successfully!", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener{
                exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
                Toast.makeText(activity, "Error while creating a board.", Toast.LENGTH_SHORT).show()
            }
    }

    fun getBoardDetails(activity: TaskListActivity, documentId: String){
        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                Log.i("Board Details", document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentId = document.id
                activity.boardDetails(board)
            }.addOnFailureListener{
                    exception ->
                Log.e("Board Details", "Error while creating a board.", exception)
            }
    }

    fun getBoardsList(activity: MainActivity){
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for (i in document.documents) {
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }
                activity.populateBoardsListToUI(boardList)
            }.addOnFailureListener{
                exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
                Toast.makeText(activity, "Error while creating a board.", Toast.LENGTH_SHORT).show()
            }
    }

    fun addUpdateTaskList(activity: Activity, board: Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "TaskList updated successfully!")
                if(activity is TaskListActivity)
                    activity.addUpdateTaskListSuccess()
                else if(activity is CardDetailsActivity)
                    activity.addUpdateTaskListSuccess()
            }.addOnFailureListener{
                    exception ->
                if(activity is TaskListActivity)
                    activity.hideProgressDialog()
                else if(activity is CardDetailsActivity)
                    activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
                Toast.makeText(activity, "Error while updating task list.", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Profile Data updated successfully!")
                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                activity.profileUpdateSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
                Toast.makeText(activity, "Error when updating the profile!", Toast.LENGTH_SHORT).show()
            }
    }
    fun loadUserData(activity: Activity, readBoardList: Boolean = false) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val loggedInUser = document.toObject(User::class.java)!!

                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess()
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser,readBoardList)
                    }
                    is MyProfileActivity -> {
                        activity.setUserDataInUI(loggedInUser)
                    }
                }

            }
            .addOnFailureListener {
                    e ->
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID

    }

    fun getAssignedMembersListDetails(activity: Activity, assignedTo: ArrayList<String>) {
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val usersList: ArrayList<User> = ArrayList()

                for (i in document.documents) {
                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }
                if (activity is MembersActivity)
                activity.setupMembersList(usersList)
                else if (activity is TaskListActivity)
                    activity.boardMembersDetailsList(usersList)
            }.addOnFailureListener{
                    exception ->
                if (activity is MembersActivity)
                activity.hideProgressDialog()
                else if (activity is TaskListActivity)
                    activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
                Toast.makeText(activity, "Error while creating a board.", Toast.LENGTH_SHORT).show()
            }
    }

    fun  getMemberDetails(activity: MembersActivity, email: String) {
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL, email)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                if (document.documents.size > 0) {
                    val user = document.documents[0].toObject(User::class.java)!!
                    activity.memberDetails(user)
                } else {
                    activity.hideProgressDialog()
                    activity.showErrorSnackBar("User not found!")
                }
            }.addOnFailureListener{
                    exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting user detail", exception)
                Toast.makeText(activity, "Error while getting user detail.", Toast.LENGTH_SHORT).show()
            }
    }

    fun assignMemberToBoard(activity: MembersActivity, board: Board, user: User) {
        val assignedToHashMap = HashMap<String, Any>()
        assignedToHashMap[Constants.ASSIGNED_TO] = board.assignedTo

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(assignedToHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Member assigned successfully!")
                activity.memberAssignSuccess(user)
            }.addOnFailureListener{
                    exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting user detail.", exception)
                Toast.makeText(activity, "Error while getting user detail.", Toast.LENGTH_SHORT).show()
            }
    }
}