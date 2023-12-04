package com.thatwaz.shoppuppet

import androidx.room.Room

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.thatwaz.shoppuppet.data.db.ShopPuppetDatabase
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private lateinit var database: SupportSQLiteDatabase
    private val TEST_DB = "migration-test"

    @Before
    fun setUp() {
        // Create an in-memory database with version 2
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            ShopPuppetDatabase::class.java
        )
            .addMigrations(ShopPuppetDatabase.MIGRATION_2_3)
            .build()
            .openHelper
            .writableDatabase
    }

    @Test
    fun testMigration() {
        // Perform the migration
        ShopPuppetDatabase.MIGRATION_2_3.migrate(database)

        // Verify the schema changes and validate data integrity
        val cursor = database.query("SELECT * FROM shops")
        assertTrue(cursor.columnNames.contains("isPriority"))

        // Additional data validation checks can be added here

        cursor.close()
    }

    @After
    fun tearDown() {
        // Close the database
        database.close()
    }
}
