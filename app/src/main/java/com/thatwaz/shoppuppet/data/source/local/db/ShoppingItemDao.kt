package com.thatwaz.shoppuppet.data.source.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.thatwaz.shoppuppet.domain.model.ShoppingItem

@Dao
interface ShoppingItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItem): Long

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

//    @Query("SELECT * FROM shopping_items")
//    suspend fun getAll(): List<ShoppingItem>
//
//    @Query("SELECT * FROM shopping_items WHERE id = :itemId")
//    suspend fun getById(itemId: Long): ShoppingItem?

    // Any other queries or operations you'd need...

}
