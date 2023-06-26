package com.example.jello_projectmanag.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jello_projectmanag.activities.TaskListActivity
import com.example.jello_projectmanag.databinding.ItemCardBinding
import com.example.jello_projectmanag.models.Card
import com.example.jello_projectmanag.models.SelectedMembers

open class CardListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Card>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            if(model.labelColor.isNotEmpty()){
                holder.viewLabelColor.visibility = android.view.View.VISIBLE
                holder.viewLabelColor.setBackgroundColor(Color
                    .parseColor(model.labelColor))
                }else{
                holder.viewLabelColor.visibility = android.view.View.GONE
            }

            holder.tvCardName.text = model.name

            if ((context as TaskListActivity).mAssignedMembersDetailList.size > 0){
                val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()

                for (i in context.mAssignedMembersDetailList.indices){
                    for (j in model.assignedTo){
                        if (context.mAssignedMembersDetailList[i].id == j){
                            val selectedMembers = SelectedMembers(
                                context.mAssignedMembersDetailList[i].id,
                                context.mAssignedMembersDetailList[i].image
                            )
                            selectedMembersList.add(selectedMembers)
                        }
                    }
                }
                if (selectedMembersList.size > 0){
                    if (selectedMembersList.size == 1 && selectedMembersList[0].id == model.createdBy){
                        holder.rvCardSelectedMembersList.visibility = android.view.View.GONE
                    }else{
                        holder.rvCardSelectedMembersList.visibility = android.view.View.VISIBLE

                        holder.rvCardSelectedMembersList.layoutManager = GridLayoutManager(
                            context,4
                        )
                        val adapter = CardMemberListItemsAdapter(context,selectedMembersList,false)
                        holder.rvCardSelectedMembersList.adapter = adapter
                        adapter.setOnClickListener(object: CardMemberListItemsAdapter.OnClickListener{
                            override fun onClick(position: Int) {
                                if (onClickListener != null){
                                    onClickListener!!.onClick(position)
                                }
                            }

                        })
                    }
                }else {
                    holder.rvCardSelectedMembersList.visibility = android.view.View.GONE
                }
            }

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }



    class MyViewHolder(binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        val tvCardName = binding.tvCardName
        val viewLabelColor = binding.viewLabelColor
        val rvCardSelectedMembersList = binding.rvCardSelectedMembersList

    }
}
