package com.thatwaz.shoppuppet.presentation.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.thatwaz.shoppuppet.databinding.FragmentShopSpecificListBinding
import com.thatwaz.shoppuppet.presentation.adapters.PurchasedItemsAdapter
import com.thatwaz.shoppuppet.presentation.adapters.ShopSpecificItemAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopSpecificListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShopSpecificListFragment : Fragment() {

    private val viewModel: ShopSpecificListViewModel by viewModels()


//    private lateinit var shopSpecificItemAdapter: ShopSpecificItemAdapter
//    private lateinit var purchasedItemsAdapter: PurchasedItemsAdapter

    private var shopSpecificItemAdapter: ShopSpecificItemAdapter? = null
    private var purchasedItemsAdapter: PurchasedItemsAdapter? = null

    private val navigationArgs: ShopSpecificListFragmentArgs by navArgs()

    private var _binding: FragmentShopSpecificListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopSpecificListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FragmentLifecycle", "onViewCreated called for ShopSpecificListFragment")

        val shopId = navigationArgs.shopId
        val shopName = navigationArgs.shopName
        val shopColor = navigationArgs.shopColorResId

        binding.tvShopName.text = shopName
        binding.clShopName.setBackgroundColor(ContextCompat.getColor(requireContext(), shopColor))

        // Set the background color of the FloatingActionButton
        binding.fabDeletePurchasedItems.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), shopColor))
        Log.d("FragmentLifecycle", "Fetching items for shop ID: $shopId")

        viewModel.fetchShopSpecificItems(shopId) // This fetches data on view creation
        observeLiveData()
        setupAdapters(shopColor)
        setupRecyclerViews()

        binding.fabDeletePurchasedItems.setOnClickListener {
            val checkedItems = purchasedItemsAdapter?.getCheckedItems() ?: listOf()
            // Pass checkedItems to ViewModel for deletion
            viewModel.deleteCheckedItems(checkedItems)

        }

        binding.btnBackToShops.setOnClickListener {
            val action = ShopSpecificListFragmentDirections
                .actionShopSpecificListFragmentToShopsFragment()
            findNavController().navigate(action)

        }


        Log.d("FragmentLifecycle", "Adapters and RecyclerViews set up, LiveData observed")


    }

    private fun createCheckboxColorStateList(shopColor: Int): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked), // unchecked
                intArrayOf(android.R.attr.state_checked) // checked
            ),
            intArrayOf(
                Color.LTGRAY, // Color for unchecked
                ContextCompat.getColor(requireContext(), shopColor) // Color for checked
            )
        )
    }


    private fun setupAdapters(shopColor: Int) {
        val colorStateList = createCheckboxColorStateList(shopColor)
        shopSpecificItemAdapter = ShopSpecificItemAdapter(colorStateList) { item ->
            if (!item.isPurchased) {
                viewModel.handleUnpurchasedItemChecked(item)
                Log.d("AdapterLog", "Unpurchased item checked: ${item.name}")
            }
        }

        purchasedItemsAdapter = PurchasedItemsAdapter(colorStateList){ item ->
            if (item.isPurchased) {
                viewModel.handlePurchasedItemChecked(item)
                Log.d("AdapterLog", "Purchased item unchecked: ${item.name}")
            }
        }
    }

    private fun setupRecyclerViews() {
        binding.rvUnpurchasedItems.apply {
            adapter = shopSpecificItemAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.rvPurchasedItems.apply {
            adapter = purchasedItemsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


    private fun observeLiveData() {
        viewModel.unpurchasedItems.observe(viewLifecycleOwner) { items ->
            shopSpecificItemAdapter?.submitList(items)
//            Log.i("DOH!","this things are: ${viewModel.purchasedItems.value}")
            Log.d(
                "FragmentLog",
                "Unpurchased items updated post-rotation: ${items.map { it.name }}"
            )
        }

        viewModel.purchasedItems.observe(viewLifecycleOwner) { items ->
            purchasedItemsAdapter?.submitList(items)
            Log.i("DOH!", "this things are: ${viewModel.purchasedItems.value}")
            Log.d("FragmentLog", "Purchased items updated post-rotation: ${items.map { it.name }}")
        }
    }

    private fun deleteCheckedItems() {
        val checkedItems = purchasedItemsAdapter?.getCheckedItems() ?: return
        viewModel.deleteCheckedItems(checkedItems)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        shopSpecificItemAdapter = null
        purchasedItemsAdapter = null
        Log.d("FragmentLifecycle", "onDestroyView called for ShopSpecificListFragment")
    }


}


//@AndroidEntryPoint
//class ShopSpecificListFragment : Fragment() {
//
//    private val viewModel: ShopSpecificListViewModel by viewModels()
//    private lateinit var shopSpecificItemAdapter: ShopSpecificItemAdapter
//    private lateinit var purchasedItemsAdapter: PurchasedItemsAdapter
//
//    private val navigationArgs: ShopSpecificListFragmentArgs by navArgs()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_shop_specific_list, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val shopId = navigationArgs.shopId
//        viewModel.fetchShopSpecificItems(shopId)
//        setupAdapters()
//        setupRecyclerViews(view)
//        observeLiveData()
//    }
//
//    private fun setupAdapters() {
//        shopSpecificItemAdapter = ShopSpecificItemAdapter { item ->
//            viewModel.handleUnpurchasedItemChecked(item)
//        }
//
//        purchasedItemsAdapter = PurchasedItemsAdapter { item ->
//            viewModel.handlePurchasedItemChecked(item)
//        }
//    }
//
//    private fun setupRecyclerViews(view: View) {
//        view.findViewById<RecyclerView>(R.id.rv_unpurchased_items).apply {
//            adapter = shopSpecificItemAdapter
//            layoutManager = LinearLayoutManager(context)
//        }
//
//        view.findViewById<RecyclerView>(R.id.rv_purchased_items).apply {
//            adapter = purchasedItemsAdapter
//            layoutManager = LinearLayoutManager(context)
//        }
//    }
//
//    private fun observeLiveData() {
//        viewModel.unpurchasedItems.observe(viewLifecycleOwner) { items ->
//            shopSpecificItemAdapter.updateUnpurchasedCheckedItems(items)
//        }
//
//        viewModel.purchasedItems.observe(viewLifecycleOwner) { items ->
//            purchasedItemsAdapter.submitItems(items)
//        }
//    }
//}

//@AndroidEntryPoint
//class ShopSpecificListFragment : Fragment() {
//
//    private val viewModel: ShopSpecificListViewModel by viewModels()
//    private lateinit var shopSpecificItemAdapter: ShopSpecificItemAdapter
//    private lateinit var purchasedItemsAdapter: PurchasedItemsAdapter // Change the type accordingly
//
//    private val navigationArgs: ShopSpecificListFragmentArgs by navArgs()
//
////    private val unpurchasedItems: MutableList<Item> = mutableListOf()
////    private val purchasedItems: MutableList<Item> = mutableListOf()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_shop_specific_list, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Access the shopId from Safe Args
//        val shopId = navigationArgs.shopId
//        Log.d("ShopSpecificListFragment", "Fetching items for shop with ID: $shopId")
//
////        viewModel.fetchShopSpecificItems(shopId)
//
//        viewModel.unpurchasedItems.observe(viewLifecycleOwner) { unpurchasedItems ->
//            shopSpecificItemAdapter.submitItems(unpurchasedItems,unpurchasedItems)
//        }
//
//        viewModel.purchasedItems.observe(viewLifecycleOwner) { purchasedItems ->
//            purchasedItemsAdapter.submitItems(purchasedItems)
//        }
//
//        // Fetch all items from the ViewModel
//        viewModel.fetchAllItems(shopId)
//
//
//
//        shopSpecificItemAdapter = ShopSpecificItemAdapter(
//            onItemCheckedListener = { item ->
//                val newCheckedItems = viewModel.shopSpecificCheckedItems.value?.toMutableList() ?: mutableListOf()
//                if (!newCheckedItems.contains(item)) {
//                    newCheckedItems.add(item)
//                } else {
//                    newCheckedItems.remove(item)
//                }
//                viewModel.updateShopSpecificCheckedItems(newCheckedItems)
//                Log.d("ShopSpecificListFragment", "ShopSpecificItemAdapter - onItemCheckedListener called")
//            }
//        )
//
//// Initialize the PurchasedItemsAdapter with the checkedItems LiveData
//        purchasedItemsAdapter = PurchasedItemsAdapter(
//            onItemCheckedListener = { item ->
//                val newCheckedItems = viewModel.purchasedCheckedItems.value?.toMutableList() ?: mutableListOf()
//                if (!newCheckedItems.contains(item)) {
//                    newCheckedItems.add(item)
//                } else {
//                    newCheckedItems.remove(item)
//                }
//                viewModel.updatePurchasedCheckedItems(newCheckedItems)
//                Log.d("ShopSpecificListFragment", "PurchasedItemsAdapter - onItemCheckedListener called")
//            }
//        )
//
//
//        // Set up the RecyclerView for shop-specific items
//        val recyclerViewShopSpecificItems =
//            view.findViewById<RecyclerView>(R.id.rv_unpurchased_items)
//        recyclerViewShopSpecificItems.adapter = shopSpecificItemAdapter
//        recyclerViewShopSpecificItems.layoutManager = LinearLayoutManager(context)
//
//        // Set up the RecyclerView for purchased items
//        val recyclerViewPurchasedItems =
//            view.findViewById<RecyclerView>(R.id.rv_purchased_items)
//        recyclerViewPurchasedItems.adapter = purchasedItemsAdapter
//        recyclerViewPurchasedItems.layoutManager = LinearLayoutManager(context)
//
//    }
//}


//@AndroidEntryPoint
//class ShopSpecificListFragment : Fragment() {
//
//    private val viewModel: ShopSpecificListViewModel by viewModels()
//    private lateinit var shopSpecificItemAdapter: ShopSpecificItemAdapter
//    private lateinit var purchasedItemsAdapter: ShopSpecificItemAdapter
//
//
//    private val navigationArgs: ShopSpecificListFragmentArgs by navArgs()
//
//    private val unpurchasedItems: MutableList<Item> = mutableListOf()
//    private val purchasedItems: MutableList<Item> = mutableListOf()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_shop_specific_list, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Access the shopId from Safe Args
//        val shopId = navigationArgs.shopId
//        Log.d("ShopSpecificListFragment", "Fetching items for shop with ID: $shopId")
//
//        viewModel.fetchShopSpecificItems(shopId)
//
//
//        // Initialize the ShopSpecificItemAdapter
////        shopSpecificItemAdapter = ShopSpecificItemAdapter()
//
//        shopSpecificItemAdapter = ShopSpecificItemAdapter { item ->
//            // Remove item from unpurchased list
//            unpurchasedItems.remove(item)
//
//            // Add item to purchased list
//            purchasedItems.add(item)
//
//            // Update RecyclerView adapters
//            shopSpecificItemAdapter.submitItems(unpurchasedItems)
//            purchasedItemAdapter.submitItems(purchasedItems)
//
//            // Initialize the purchasedItemsAdapter
//            purchasedItemsAdapter = ShopSpecificItemAdapter { item ->
//                // Remove item from purchased list
//                purchasedItems.remove(item)
//
//                // Add item back to the unpurchased list
//                unpurchasedItems.add(item)
//
//                // Update RecyclerView adapters
//                purchasedItemsAdapter.submitItems(purchasedItems)
//                shopSpecificItemAdapter.submitItems(unpurchasedItems)
//            }
//
//
//            // Set up the RecyclerView for shop-specific items
//            val recyclerViewShopSpecificItems =
//                view.findViewById<RecyclerView>(R.id.rv_unpurchased_items)
//            recyclerViewShopSpecificItems.adapter = shopSpecificItemAdapter
//            recyclerViewShopSpecificItems.layoutManager = LinearLayoutManager(context)
//
//            // Observe the shop-specific items from the ViewModel
//            viewModel.shopSpecificItems.observe(viewLifecycleOwner) { shopSpecificItems ->
//                shopSpecificItemAdapter.submitItems(shopSpecificItems)
//                Log.i("DOH!", "Submitted items are$shopSpecificItems")
//            }
//        }
//    }
//}


//@AndroidEntryPoint
//class ShopSpecificListFragment : Fragment() {
//
//    private val viewModel: ShopSpecificListViewModel by viewModels()
//    private lateinit var shopSpecificItemAdapter: ShopSpecificItemAdapter
//
//    private val navigationArgs: ShopSpecificListFragmentArgs by navArgs()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_shop_specific_list, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        val shopId = arguments?.getLong("shopId", 0L) // Get the shop ID as a long with a default value of 0
//
//
//        Log.d("ShopSpecificListFragment", "Fetching items for shop with ID: $shopId")
//
//        if (shopId != null) {
//            viewModel.fetchShopSpecificItems(shopId)
//        }
//
//        // Initialize the ShopSpecificItemAdapter
//        shopSpecificItemAdapter = ShopSpecificItemAdapter()
//
//        // Set up the RecyclerView for shop-specific items
//        val recyclerViewShopSpecificItems =
//            view.findViewById<RecyclerView>(R.id.rv_unpurchased_items)
//        recyclerViewShopSpecificItems.adapter = shopSpecificItemAdapter
//        recyclerViewShopSpecificItems.layoutManager = LinearLayoutManager(context)
//
//        // Observe the shop-specific items from the ViewModel
//        viewModel.shopSpecificItems.observe(viewLifecycleOwner) { shopSpecificItems ->
//            shopSpecificItemAdapter.submitItems(shopSpecificItems)
//        }
//    }
//}


//@AndroidEntryPoint
//class ShopSpecificListFragment : Fragment() {
//
//    private val viewModel: ShopSpecificListViewModel by viewModels()
//
//    private lateinit var adapter: ItemAdapter
//
//    //    val shopId = requireArguments().getLong("shopId")
////    val args: ShopSpecificListFragmentArgs by navArgs()
////    val shopId = args.shopId
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_shop_specific_list, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize your adapter
////        adapter = ItemAdapter()
//
//        // Set up the RecyclerView
//        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_unpurchased_items)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(context)
//
////        viewModel.items.observe(viewLifecycleOwner) { items ->
////            unpurchasedAdapter.submitList(items.filter { !it.isPurchased })
////            purchasedAdapter.submitList(items.filter { it.isPurchased })
////        }
//
//        viewModel.items.observe(viewLifecycleOwner) { items ->
//            // Filter items to show only unpurchased items
//            val unpurchasedItems = items.filter { !it.isPurchased }
//            adapter.submitList(unpurchasedItems)
//        }
//
//
//        // Observe the items from the ViewModel
//        viewModel.items.observe(viewLifecycleOwner) { items ->
//            adapter.submitList(items)
//        }
//
//        // Fetch the items for a specific shop
//        // Note: You need to provide the shop ID somehow, e.g., through arguments
//        val shopId = requireArguments().getLong("shopId")
//        viewModel.fetchItemsForShop(shopId)
//    }
//}


