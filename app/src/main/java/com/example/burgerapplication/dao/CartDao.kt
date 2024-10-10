package com.example.burgerapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.burgerapplication.entity.CartEntity

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartEntity)

    @Query("SELECT quantity FROM cart_items WHERE productId = :productId")
    suspend fun getQuantityByProductId(productId: Int): Int?

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteByProductId(productId: Int)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartEntity>

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}