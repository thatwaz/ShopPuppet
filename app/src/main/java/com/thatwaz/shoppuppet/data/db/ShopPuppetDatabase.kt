package com.thatwaz.shoppuppet.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thatwaz.shoppuppet.data.db.converter.Converters
import com.thatwaz.shoppuppet.data.db.dao.ItemDao
import com.thatwaz.shoppuppet.data.db.dao.ShopDao
import com.thatwaz.shoppuppet.domain.model.Item
import com.thatwaz.shoppuppet.domain.model.Shop

@Database(entities = [Item::class, Shop::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ShopPuppetDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: ShopPuppetDatabase? = null

        fun getDatabase(context: Context): ShopPuppetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopPuppetDatabase::class.java,
                    "shop_puppet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
