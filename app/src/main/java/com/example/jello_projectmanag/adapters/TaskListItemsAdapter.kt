package com.example.jello_projectmanag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jello_projectmanag.activities.TaskListActivity
import com.example.jello_projectmanag.databinding.ItemTaskBinding
import com.example.jello_projectmanag.models.Task

open class TaskListItemsAdapter(private val context: Context,
                           private var list: ArrayList<Task>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(
            (15.toDp().toPx()),
            0,
            (40.toDp().toPx()),
            0
        )
        binding.root.layoutParams = layoutParams
        return MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder) {
            if (position == list.size - 1) {
                holder.tvAddTaskList.visibility = ViewGroup.VISIBLE
                holder.llTaskItem.visibility = ViewGroup.GONE
            }else{
                holder.tvAddTaskList.visibility = ViewGroup.GONE
                holder.llTaskItem.visibility = ViewGroup.VISIBLE
            }
            holder.tvTaskListTitle.text = model.title
            holder.tvAddTaskList.setOnClickListener{
                holder.tvAddTaskList.visibility = ViewGroup.GONE
                holder.cvAddTaskListName.visibility = ViewGroup.VISIBLE
            }

            // Set the click event for close list button.
            holder.ibCloseListName.setOnClickListener{
                holder.tvAddTaskList.visibility = ViewGroup.VISIBLE
                holder.cvAddTaskListName.visibility = ViewGroup.GONE
            }

            // Set the click event for done list button.
            holder.ibDoneListName.setOnClickListener{
                val listName = holder.etTaskListName.text.toString()

                if(listName.isNotEmpty()) {
                    if (context is TaskListActivity) {
                        context.createTaskList(listName)
                    }
                }else{
                    holder.etTaskListName.error = "Please enter a list name"
                }
            }

            // Set the click event for edit list button.
                holder.ibEditListName.setOnClickListener {
                    holder.etTaskListName.setText(model.title)
                    holder.llTitleView.visibility = ViewGroup.GONE
                    holder.cvAddTaskListName.visibility = ViewGroup.VISIBLE
                }

            // Set the click event for close edit list button.
            holder.ibCloseEditableView.setOnClickListener{
                holder.llTitleView.visibility = ViewGroup.VISIBLE
                holder.cvAddTaskListName.visibility = ViewGroup.GONE
            }

            // Set the click event for done edit list button.
            holder.ibDoneEditListName.setOnClickListener {
                val listName = holder.etTaskListName.text.toString()
                if(listName.isNotEmpty()) {
                    if (context is TaskListActivity) {
                        context.updateTaskList(position, listName, model)
                    }
                }else{
                    holder.etTaskListName.error = "Please enter a list name"
                }
            }

            // Set the click event for delete list button.
            holder.ibDeleteList.setOnClickListener{
                alertDialogForDeleteList(position, model.title)
            }

            // Set the click event for add card button.
            holder.tvAddCard.setOnClickListener{
                holder.tvAddCard.visibility = ViewGroup.GONE
                holder.cvAddCard.visibility = ViewGroup.VISIBLE
            }

            // Set the click event for close card button.
            holder.ibCloseCardName.setOnClickListener{
                holder.tvAddCard.visibility = ViewGroup.VISIBLE
                holder.cvAddCard.visibility = ViewGroup.GONE
            }

            // Set the click event for done card button.
            holder.ibDoneCardName.setOnClickListener {
                val cardName = holder.etCardName.text.toString()
                if(cardName.isNotEmpty()){
                    if(context is TaskListActivity){
                        context.addCardToTaskList(position, cardName)
                    }
                }else{
                    holder.etCardName.error = "Please enter a card name"
                }
            }

            // Set RecyclerView for cards list.
            holder.rvCardList.layoutManager = LinearLayoutManager(context)

            holder.rvCardList.setHasFixedSize(true)
            val adapter = CardListItemsAdapter(context, model.cards)
            holder.rvCardList.adapter = adapter
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun alertDialogForDeleteList(position: Int, title: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete $title?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){ dialogInterface, _ ->
            dialogInterface.dismiss()
            if(context is TaskListActivity){
                context.deleteTaskList(position)
            }
        }
        builder.setNegativeButton("No"){ dialogInterface, _ ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    // Get dp from pixels
    private fun Int.toDp(): Int =
        (this / context.resources.displayMetrics.density).toInt()

    // Get pixels from dp
    private fun Int.toPx(): Int =
        (this * context.resources.displayMetrics.density).toInt()


    private class MyViewHolder(binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){
        val tvAddTaskList = binding.tvAddTaskList
        val llTaskItem = binding.llTaskItem
        val llTitleView = binding.llTitleView
        val tvTaskListTitle = binding.tvTaskListTitle
        val tvAddCard = binding.tvAddCard
        val cvAddTaskListName = binding.cvAddTaskListName
        val cvAddCard = binding.cvAddCard
        val ibCloseListName = binding.ibCloseListName
        val ibDoneListName = binding.ibDoneListName
        val ibEditListName = binding.ibEditListName
        val ibDeleteList = binding.ibDeleteList
        val ibCloseCardName = binding.ibCloseCardName
        val ibCloseEditableView = binding.ibCloseEditableView
        val ibDoneEditListName = binding.ibDoneEditListName
        val ibDoneCardName = binding.ibDoneCardName
        val etTaskListName = binding.etTaskListName
        val etCardName = binding.etCardName
        val rvCardList = binding.rvCardList
    }
}
