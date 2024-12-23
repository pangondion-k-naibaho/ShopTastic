package com.shoptastic.client.ui.rv_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoptastic.client.R
import com.shoptastic.client.data.model.other.ProductSavedWithCount
import com.shoptastic.client.databinding.ItemRvSavedProductLayoutBinding
import com.shoptastic.client.utils.Extension.Companion.toCurrencyString

class ItemSavedProductAdapter(
    var data: MutableList<ProductSavedWithCount>,
    private val listener: ItemListener,
    private val onSelectionChanged: (Boolean) -> Unit
): RecyclerView.Adapter<ItemSavedProductAdapter.ViewHolder>() {
    interface ItemListener{
        fun onBoxChecked(item: ProductSavedWithCount, price: Double)

        fun onBoxUnchecked(item: ProductSavedWithCount, price: Double)

        fun onAccessItem(id: Int)

//        fun onItemCheckedStatusChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: ProductSavedWithCount, listener: ItemListener) = with(itemView){
            val binding = ItemRvSavedProductLayoutBinding.bind(itemView)

            Glide.with(itemView.context)
                .load(item.savedProduct.image)
                .into(binding.ivProduct)

            binding.tvProductName.text = item.savedProduct.title
            binding.tvProductPrice.text = item.savedProduct.price.toCurrencyString()

            binding.tvCountProduct.apply {
                text = item.count.toString()
                visibility = when{
                    item.count >=2 -> View.VISIBLE
                    else -> View.GONE
                }
            }

            binding.tvClickHere.setOnClickListener {
                listener.onAccessItem(item.savedProduct.id)
            }

            binding.cbProduct.isChecked = item.isChecklisted

            binding.cbProduct.apply {
                item.isChecklisted = isChecked

                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        listener.onBoxChecked(item, item.savedProduct.price * item.count)
                        updateItemCheckStatus(item, true)
                    } else {
                        listener.onBoxUnchecked(item, item.savedProduct.price * item.count)
                        updateItemCheckStatus(item, false)
                    }
                    // Panggil listener untuk memberitahukan perubahan
//                    listener.onItemCheckedStatusChanged()

                    val allChecked = data.all { it.isChecklisted }
                    onSelectionChanged(allChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_saved_product_layout, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    private fun updateItemCheckStatus(item: ProductSavedWithCount, isChecked: Boolean) {
        // Find the item in the list and update its 'isChecklisted' value
        val index = data.indexOf(item)
        if (index != -1) {
            data[index] = item.copy(isChecklisted = isChecked)
        }
    }

    fun checkAllItems(isChecked: Boolean) {
        var total = 0.0
        for (i in data.indices) {
            val item = data[i]
            if (item.isChecklisted != isChecked) {
                data[i] = item.copy(isChecklisted = isChecked)
            }
        }
        notifyDataSetChanged()
    }

    fun setSelectAll(isChecked: Boolean) {
        data.forEach { it.isChecklisted = isChecked }
        notifyDataSetChanged() // Refresh adapter agar tampilan berubah
    }

    fun areAllItemsChecked(): Boolean {
        return data.all { it.isChecklisted }
    }
}