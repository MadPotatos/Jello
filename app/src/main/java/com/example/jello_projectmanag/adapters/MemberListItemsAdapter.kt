package com.example.jello_projectmanag.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jello_projectmanag.R
import com.example.jello_projectmanag.databinding.ItemMemberBinding
import com.example.jello_projectmanag.models.User
import com.example.jello_projectmanag.utils.Constants


open class MemberListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<User>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemMemberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.ivMemberImage)

            holder.tvMemberName.text = model.name
            holder.tvMemberEmail.text = model.email

            if(model.selected){
                holder.ivSelectedMember.visibility = android.view.View.VISIBLE
            }else{
                holder.ivSelectedMember.visibility = android.view.View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    if(model.selected){
                        onClickListener!!.onClick(position,model,Constants.UN_SELECT)
                    }else{
                        onClickListener!!.onClick(position,model,Constants.SELECT)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }


    class MyViewHolder(binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root){
        val tvMemberName = binding.tvMemberName
        val tvMemberEmail = binding.tvMemberEmail
        val ivMemberImage = binding.ivMemberImage
        val ivSelectedMember = binding.ivSelectedMember
    }
}