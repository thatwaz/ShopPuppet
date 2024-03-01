package com.thatwaz.shoppuppet.presentation.viewmodel


//class ItemViewModelTest {
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var viewModel: ItemViewModel
//    private val itemRepository = mock(ItemRepository::class.java)
//
//    @Before
//    fun setup() {
//        viewModel = ItemViewModel(itemRepository)
//    }
//
//    @Test
//    fun fetchAllItems_updatesLiveData() {
//        // Define your test data and expected results here
//        val testData = listOf(Item(id = 1, name = "Item 1"), Item(id = 2, name = "Item 2"))
//        val expectedLiveDataValue = MutableLiveData<List<Item>>().apply { value = testData }
//
//        // Mock repository behavior
//        `when`(itemRepository.getAllItems()).thenReturn(expectedLiveDataValue)
//
//        // Call the ViewModel function to be tested
//        viewModel.fetchAllItems()
//
//        // Assert that the LiveData is updated with the expected data
//        assertEquals(expectedLiveDataValue.value, viewModel.items.value)
//    }
//}
