package com.shoptastic.client.utils

import android.content.Context
import com.shoptastic.client.data.model.other.ItemDropdown

class Extension {
    companion object{
        fun Int.dpToPx(context: Context): Int {
            val scale = context.resources.displayMetrics.density
            return (this * scale + 0.5f).toInt()
        }

        fun retrieveListItemDropdownStatus(input: List<String>): List<ItemDropdown>{
            val arrListItemDropdown = ArrayList<ItemDropdown>()

            for(i in input.indices){
                val itemDropdown = ItemDropdown(
                    name = input[i]
                )
                arrListItemDropdown.add(itemDropdown)
            }

            return arrListItemDropdown
        }

        fun Double.toCurrencyString(): String {
            return "$ %.2f".format(this)
        }

    }
}