package com.thatwaz.shoppuppet.di

import android.content.Context
import com.thatwaz.shoppuppet.data.db.ShopPuppetDatabase
import com.thatwaz.shoppuppet.data.db.dao.ItemDao
import com.thatwaz.shoppuppet.data.db.dao.ItemShopCrossRefDao
import com.thatwaz.shoppuppet.data.db.dao.ShopDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ShopPuppetDatabase {
        return ShopPuppetDatabase.getDatabase(appContext)
    }

    @Provides
    fun provideItemDao(database: ShopPuppetDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    fun provideShopDao(database: ShopPuppetDatabase): ShopDao {
        return database.shopDao()
    }

    @Provides
    fun provideItemShopCrossRefDao(database: ShopPuppetDatabase): ItemShopCrossRefDao {
        return database.itemShopCrossRefDao()
    }

}
