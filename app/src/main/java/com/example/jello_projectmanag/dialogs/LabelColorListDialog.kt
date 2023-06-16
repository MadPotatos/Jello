package com.example.jello_projectmanag.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jello_projectmanag.adapters.LabelColorListItemsAdapter
import com.example.jello_projectmanag.databinding.DialogListBinding

abstract class LabelColorListDialog(
    context: Context,
    private var list: ArrayList<String>,
    private var title: String = "",
    private var mSelectedColor: String = ""
 ):Dialog(context) {

    private var adapter: LabelColorListItemsAdapter? = null
    private lateinit var binding: DialogListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCanceledOnTouchOutside(true)
        setCancelable(true)

        setUpRecyclerView()

    }

    private fun setUpRecyclerView(){
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(context)
        adapter = LabelColorListItemsAdapter(context, list, mSelectedColor)
        binding.rvList.adapter = adapter

        adapter!!.onClickListener = object : LabelColorListItemsAdapter.OnClickListener{
            override fun onClick(position: Int, color: String) {
                dismiss()
                onItemSelected(color)
            }
        }
    }

    protected abstract fun onItemSelected(color: String)
}