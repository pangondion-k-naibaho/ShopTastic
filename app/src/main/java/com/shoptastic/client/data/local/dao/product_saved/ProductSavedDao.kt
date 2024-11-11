package com.shoptastic.client.data.local.dao.product_saved

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shoptastic.client.data.model.other.ProductSaved

@Dao
interface ProductSavedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: ProductSaved)

    @Query("SELECT * FROM ProductSaved")
    suspend fun getAllProducts(): List<ProductSaved>

    @Delete
    suspend fun deleteProduct(product: ProductSaved)
}