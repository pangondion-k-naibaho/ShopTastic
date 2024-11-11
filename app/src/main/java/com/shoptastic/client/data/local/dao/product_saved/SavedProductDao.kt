package com.shoptastic.client.data.local.dao.product_saved

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shoptastic.client.data.model.other.ProductSaved
import com.shoptastic.client.data.model.response.products.ProductResponse

@Dao
interface SavedProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: ProductResponse)

    @Query("SELECT * FROM ProductResponse")
    suspend fun getAllProducts(): List<ProductResponse>

    @Delete
    suspend fun deleteProduct(product: ProductResponse)
}