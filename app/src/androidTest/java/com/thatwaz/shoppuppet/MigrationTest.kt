package com.thatwaz.shoppuppet

//@RunWith(AndroidJUnit4::class)
//class MigrationTest {
//
//    private lateinit var database: SupportSQLiteDatabase
//    private val TEST_DB = "migration-test"
//
//    @Before
//    fun setUp() {
//        // Create an in-memory database with version 3
//        database = Room.inMemoryDatabaseBuilder(
//            InstrumentationRegistry.getInstrumentation().targetContext,
//            ShopPuppetDatabase::class.java
//        )
//            .addMigrations(ShopPuppetDatabase.MIGRATION_3_4)
//            .build()
//            .openHelper
//            .writableDatabase
//    }
//
//    @Test
//    fun testMigration() {
//        // Perform the migration from version 3 to 4
//        ShopPuppetDatabase.MIGRATION_3_4.migrate(database)
//
//        // Verify the schema changes and validate data integrity
//        val cursor = database.query("SELECT * FROM items")
//        assertTrue(cursor.columnNames.contains("isPriorityItem"))
//
//        // Additional data validation checks can be added here
//
//        cursor.close()
//    }
//
//    @After
//    fun tearDown() {
//        // Close the database
//        database.close()
//    }
//}




//@RunWith(AndroidJUnit4::class)
//class MigrationTest {
//
//    private lateinit var database: SupportSQLiteDatabase
//    private val TEST_DB = "migration-test"
//
//    @Before
//    fun setUp() {
//        // Create an in-memory database with version 2
//        database = Room.inMemoryDatabaseBuilder(
//            InstrumentationRegistry.getInstrumentation().targetContext,
//            ShopPuppetDatabase::class.java
//        )
//            .addMigrations(ShopPuppetDatabase.MIGRATION_2_3)
//            .build()
//            .openHelper
//            .writableDatabase
//    }
//
//    @Test
//    fun testMigration() {
//        // Perform the migration
//        ShopPuppetDatabase.MIGRATION_2_3.migrate(database)
//
//        // Verify the schema changes and validate data integrity
//        val cursor = database.query("SELECT * FROM shops")
//        assertTrue(cursor.columnNames.contains("isPriority"))
//
//        // Additional data validation checks can be added here
//
//        cursor.close()
//    }
//
//    @After
//    fun tearDown() {
//        // Close the database
//        database.close()
//    }
//}
