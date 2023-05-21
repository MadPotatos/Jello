package com.example.jello_projectmanag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.jello_projectmanag.databinding.ItemTaskBinding
import com.example.jello_projectmanag.models.Task

class TaskListItemsAdapter(private val context: Context,
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
        }
    }

    override fun getItemCount(): Int {
        return list.size
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

    }
}
