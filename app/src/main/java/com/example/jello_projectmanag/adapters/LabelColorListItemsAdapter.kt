package com.example.jello_projectmanag.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jello_projectmanag.databinding.ItemLabelColorBinding

class LabelColorListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<String>,
    private var mSelectedColor: String
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemLabelColorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if(holder is MyViewHolder){
            holder.viewMain.setBackgroundColor(Color.parseColor(item))

            if(item == mSelectedColor){
                holder.ivSelectedColor.visibility = android.view.View.VISIBLE
            }else{
                holder.ivSelectedColor.visibility = android.view.View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position, item)
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, color: String)
    }
    private class MyViewHolder(binding: ItemLabelColorBinding) : RecyclerView.ViewHolder(binding.root){
        val viewMain = binding.viewMain
        val ivSelectedColor = binding.ivSelectedColor
    }
}