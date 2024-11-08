package com.shoptastic.client.ui.rv_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shoptastic.client.R
import com.shoptastic.client.data.model.other.ItemDropdown
import com.shoptastic.client.databinding.ItemRvDropdownLayoutBinding

class ItemDropdownAdapter(
    var data: List<ItemDropdown>,
    private val listener: ItemListener?,
): RecyclerView.Adapter<ItemDropdownAdapter.ViewHolder>() {
    interface ItemListener{
        fun onClick(item: ItemDropdown, position: Int)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: ItemDropdown, listener: ItemListener?, position: Int) = with(itemView){
            val binding = ItemRvDropdownLayoutBinding.bind(itemView)
            binding.tvItemDropdownName.text = item.name
            when(item.isSelected){
                true -> binding.tvItemDropdownName.setTextColor(ContextCompat.getColor(itemView.context, R.color.bleu_de_france))
                false -> binding.tvItemDropdownName.setTextColor(ContextCompat.getColor(itemView.context, R.color.void_century))
            }
            binding.root.setOnClickListener { listener?.onClick(item, position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_dropdown_layout, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], listener, position)
    }

    fun updateChecked(item: ItemDropdown){
        data.mapIndexed { _, data -> data.isSelected = data.name == item.name }
        item.isSelected = true
        notifyDataSetChanged()
    }
}