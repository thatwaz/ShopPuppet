package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.presentation.adapters.ItemAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopSpecificListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopSpecificListFragment : Fragment() {

    private val viewModel: ShopSpecificListViewModel by viewModels()

    private lateinit var adapter: ItemAdapter

    //    val shopId = requireArguments().getLong("shopId")
//    val args: ShopSpecificListFragmentArgs by navArgs()
//    val shopId = args.shopId


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_specific_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize your adapter
//        adapter = ItemAdapter()

        // Set up the RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_unpurchased_items)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

//        viewModel.items.observe(viewLifecycleOwner) { items ->
//            unpurchasedAdapter.submitList(items.filter { !it.isPurchased })
//            purchasedAdapter.submitList(items.filter { it.isPurchased })
//        }


        // Observe the items from the ViewModel
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        // Fetch the items for a specific shop
        // Note: You need to provide the shop ID somehow, e.g., through arguments
        val shopId = requireArguments().getLong("shopId")
        viewModel.fetchItemsForShop(shopId)
    }
}


