package com.shoptastic.client.ui.rv_adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shoptastic.client.data.model.other.ProductSavedWithCount

class ItemSavedProductAdapter(
    var data: MutableList<ProductSavedWithCount>,
    private val listener: ItemListener,
): RecyclerView.Adapter<ItemSavedProductAdapter.ViewHolder>() {
    interface ItemListener{
        fun onBoxChecked(price: Double)

        fun onAccessItem(id: String)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}