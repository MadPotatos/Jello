package com.example.jello_projectmanag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jello_projectmanag.R

import com.example.jello_projectmanag.databinding.ItemCardSelectedMemberBinding

import com.example.jello_projectmanag.models.SelectedMembers

open class CardMemberListItemsAdapter (
    private val context: Context,
    private val list: ArrayList<SelectedMembers>,
    private val assignedMembers: Boolean
    ):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemCardSelectedMemberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            if (position == list.size - 1 && assignedMembers) {
                holder.ivAddMember.visibility = android.view.View.VISIBLE
                holder.ivSelectedMemberImage.visibility = android.view.View.GONE
            }else{
                holder.ivAddMember.visibility = android.view.View.GONE
                holder.ivSelectedMemberImage.visibility = android.view.View.VISIBLE

                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.ivSelectedMemberImage)
            }

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position)
                }
            }
        }
    }


        interface OnClickListener {
            fun onClick(position: Int)
        }


        fun setOnClickListener(onClickListener: OnClickListener) {
            this.onClickListener = onClickListener
        }


        class MyViewHolder(binding: ItemCardSelectedMemberBinding) : RecyclerView.ViewHolder(binding.root) {
            val ivSelectedMemberImage = binding.ivSelectedMemberImage
            val ivAddMember = binding.ivAddMember

        }
    }
