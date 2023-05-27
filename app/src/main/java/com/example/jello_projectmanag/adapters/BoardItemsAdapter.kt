package com.example.jello_projectmanag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.databinding.ItemBoardBinding
import com.example.jello_projectmanag.models.Board

open class BoardItemsAdapter(private val context: Context,
                             private var list: ArrayList<Board>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemBoardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
                Glide.with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(holder.boardImage)

            holder.tvName.text = model.name
            "Created by: ${model.createdBy}".also { holder.tvCreatedBy.text = it }

            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }


    interface OnClickListener {
        fun onClick(position: Int,model: Board)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    private class MyViewHolder(binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root){
        val boardImage = binding.ivBoardImage
        val tvName = binding.tvName
        val tvCreatedBy = binding.tvCreatedBy
    }
}