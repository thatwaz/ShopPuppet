package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.presentation.adapters.ShopSpecificItemAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopSpecificListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShopSpecificListFragment : Fragment() {

    private val viewModel: ShopSpecificListViewModel by viewModels()
    private lateinit var shopSpecificItemAdapter: ShopSpecificItemAdapter

    private val navigationArgs: ShopSpecificListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_specific_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the shopId from Safe Args
        val shopId = navigationArgs.shopId
        Log.d("ShopSpecificListFragment", "Fetching items for shop with ID: $shopId")

        viewModel.fetchShopSpecificItems(shopId)


        // Initialize the ShopSpecificItemAdapter
        shopSpecificItemAdapter = ShopSpecificItemAdapter()

        // Set up the RecyclerView for shop-specific items
        val recyclerViewShopSpecificItems =
            view.findViewById<RecyclerView>(R.id.rv_unpurchased_items)
        recyclerViewShopSpecificItems.adapter = shopSpecificItemAdapter
        recyclerViewShopSpecificItems.layoutManager = LinearLayoutManager(context)

        // Observe the shop-specific items from the ViewModel
        viewModel.shopSpecificItems.observe(viewLifecycleOwner) { shopSpecificItems ->
            shopSpecificItemAdapter.submitItems(shopSpecificItems)
            Log.i("DOH!","Submitted items are$shopSpecificItems")
        }
    }
}





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


