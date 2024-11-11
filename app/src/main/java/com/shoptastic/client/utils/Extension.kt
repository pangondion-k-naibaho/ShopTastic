package com.shoptastic.client.utils

import android.content.Context
import com.shoptastic.client.data.model.other.ItemDropdown
import com.shoptastic.client.data.model.other.ProductSavedWithCount
import com.shoptastic.client.data.model.response.products.ProductResponse
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

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

        fun generateDataId(): String {
            val prefix = "shptstc"

            val currentTime = SimpleDateFormat("ddMMyyyyHHmm").format(Date())

            val randomNumber = Random.nextLong(100_000_000L, 999_999_999L)

            return "$prefix-$currentTime-$randomNumber"
        }

        fun List<ProductResponse>.toProductSavedWithCount(): List<ProductSavedWithCount> {
            // Membuat map untuk mengelompokkan produk berdasarkan id-nya
            val groupedByProduct = this.groupBy { it.id }

            // Mengonversi hasil pengelompokkan menjadi list ProductSavedWithCount
            return groupedByProduct.map { (productId, products) ->
                // Ambil salah satu produk dari kelompok yang sama (semua produk di kelompok ini sama)
                val product = products.first()

                // Hitung berapa banyak produk yang sama
                val count = products.size

                // Kembalikan ProductSavedWithCount dengan count yang sesuai
                ProductSavedWithCount(savedProduct = product, count = count)
            }
        }

    }
}