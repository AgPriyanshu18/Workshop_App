package com.example.workshopapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workshopapp.databinding.WorkshopItemViewBinding
import com.example.workshopapp.models.workshop

class workshopListAdapter (
    private var list : ArrayList<workshop>,
    private var ch : Boolean) :
    RecyclerView.Adapter<workshopListAdapter.ItemWorkshopHolder>() {

    class ItemWorkshopHolder(binding: WorkshopItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.wkName
        val desc = binding.wkDesc
        val fees = binding.wkFees
        var date = binding.wkDate
        val applyBtn = binding.applyBtn    }

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWorkshopHolder {
        return ItemWorkshopHolder(
            WorkshopItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemWorkshopHolder, position: Int) {
        val model = list[position]

        holder.date.text = model.date
        holder.name.text = model.workshopName
        holder.desc.text = model.workshopDesc
        holder.fees.text = "Fee - Rs " + model.fees.toString()

        if(ch){
            holder.applyBtn.visibility = View.GONE
        }else{
            holder.applyBtn.visibility = View.VISIBLE
        }
        holder.applyBtn.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
        }
    }


    interface OnClickListener {
        fun onClick(position: Int, workshop: workshop)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    override fun getItemCount(): Int {
        return list.size
    }

}