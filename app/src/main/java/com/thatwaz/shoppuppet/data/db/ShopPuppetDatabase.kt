package com.thatwaz.shoppuppet.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thatwaz.shoppuppet.data.db.converter.Converters
import com.thatwaz.shoppuppet.data.db.dao.ItemDao
import com.thatwaz.shoppuppet.data.db.dao.ItemShopCrossRefDao
import com.thatwaz.shoppuppet.data.db.dao.ShopDao
import com.thatwaz.shoppuppet.domain.model.Item
import com.thatwaz.shoppuppet.domain.model.ItemShopCrossRef
import com.thatwaz.shoppuppet.domain.model.Shop

// todo need migration strategy
@Database(entities = [Item::class, Shop::class, ItemShopCrossRef::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ShopPuppetDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun shopDao(): ShopDao
    abstract fun itemShopCrossRefDao(): ItemShopCrossRefDao

    companion object {
        @Volatile
        private var INSTANCE: ShopPuppetDatabase? = null

        fun getDatabase(context: Context): ShopPuppetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopPuppetDatabase::class.java,
                    "shop_puppet_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}



