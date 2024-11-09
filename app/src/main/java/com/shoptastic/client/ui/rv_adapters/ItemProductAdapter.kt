package com.shoptastic.client.ui.rv_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoptastic.client.R
import com.shoptastic.client.data.Constants.PRODUCT_CATEGORY.Companion.ELECTRONICS
import com.shoptastic.client.data.Constants.PRODUCT_CATEGORY.Companion.JEWELRY
import com.shoptastic.client.data.Constants.PRODUCT_CATEGORY.Companion.MENS_CLOTHING
import com.shoptastic.client.data.Constants.PRODUCT_CATEGORY.Companion.WOMENS_CLOTHING
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.databinding.ItemRvProductLayoutBinding
import com.shoptastic.client.utils.Extension.Companion.toCurrencyString

class ItemProductAdapter(
    var data: MutableList<ProductResponse>,
    private val listener: ItemListener,
): RecyclerView.Adapter<ItemProductAdapter.ViewHolder>() {
    interface ItemListener{
        fun onClick(item: ProductResponse)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: ProductResponse, listener: ItemListener) = with(itemView){
            val binding = ItemRvProductLayoutBinding.bind(itemView)

            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.ivProduct)

            binding.tvProductName.text = item.title
            binding.tvProductPrice.text = item.price.toCurrencyString()
            binding.tvProductRating.text = item.rating.rate.toString()
            binding.tvProductCategory.apply {
                text = item.category
                val textColor = when(item.category){
                    ELECTRONICS -> R.color.grenadine_pink
                    JEWELRY -> R.color.droplet
                    MENS_CLOTHING -> R.color.aqua_fiesta
                    WOMENS_CLOTHING -> R.color.dwarven_peaches
                    else -> R.color.black
                }
                setTextColor(ContextCompat.getColor(itemView.context, textColor))
            }

            binding.root.setOnClickListener { listener.onClick(item) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_product_layout, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    fun updateItem(listProduct: List<ProductResponse>){
        try{
            if(listProduct != null){
                data.clear()
                data.addAll(listProduct)
                notifyDataSetChanged()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}