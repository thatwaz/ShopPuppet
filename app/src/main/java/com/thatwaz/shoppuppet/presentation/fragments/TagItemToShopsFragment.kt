package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentTagItemToShopsBinding
import com.thatwaz.shoppuppet.presentation.adapters.ShopSelectionAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.SelectedShopsViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.TagItemToShopsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TagItemToShopsFragment : Fragment(R.layout.fragment_tag_item_to_shops) {

    private var _binding: FragmentTagItemToShopsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TagItemToShopsViewModel by viewModels()
    private val selectedShopsViewModel: SelectedShopsViewModel by viewModels()
    private lateinit var shopSelectionAdapter: ShopSelectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTagItemToShopsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TagItemToShopsFragment", "onViewCreated called")

        setupRecyclerView()
        observeShopData()

        shopSelectionAdapter.onItemClick = { selectedShop ->
            selectedShopsViewModel.addSelectedShop(selectedShop)
        }

        // ...
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvShopsToTag
        recyclerView.layoutManager = LinearLayoutManager(context)
        shopSelectionAdapter = ShopSelectionAdapter(
            onItemClick = { selectedShop ->
                selectedShopsViewModel.addSelectedShop(selectedShop)
            },
            selectedShopsViewModel = selectedShopsViewModel
        )
        recyclerView.adapter = shopSelectionAdapter
    }

    private fun observeShopData() {
        viewModel.shops.observe(viewLifecycleOwner) { shops ->
            Log.d("DOH!", "Shops are this $shops")
            shopSelectionAdapter.submitList(shops)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




//@AndroidEntryPoint
//class TagItemToShopsFragment : Fragment(R.layout.fragment_tag_item_to_shops) {
//
//    // ViewModel for handling data and logic related to this fragment
//    private val viewModel: TagItemToShopsViewModel by viewModels()
//
//    // ViewModel for managing selected shops
//    private val selectedShopsViewModel: SelectedShopsViewModel by viewModels()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        Log.d("TagItemToShopsFragment", "onViewCreated called")
//
//        // Observe the selected shops using the new ViewModel
////        viewModel.shops.observe(viewLifecycleOwner) { selectedShops ->
////            Log.d("TagItemToShopsFragment", "Selected Shops: $selectedShops")
////            // Handle selected shops as needed
////        }
//
//        // Initialize the RecyclerView
//        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_shops_to_tag)
//
//        val adapter = ShopSelectionAdapter(
//            onItemClick = { selectedShop ->
//                // Handle shop selection here, e.g., store selectedShop in a ViewModel
//                selectedShopsViewModel.addSelectedShop(selectedShop)
//            },
//            selectedShopsViewModel = selectedShopsViewModel
//        )
//
//        // Observe the shops LiveData from TagItemToShopsViewModel
//        viewModel.shops.observe(viewLifecycleOwner) { shops ->
//            // Update your UI with the list of shops
//            Log.d("DOH!","Shops are this $shops")
//            adapter.submitList(shops)
//        }
//
//
//
//        // Set the adapter for the RecyclerView
//        recyclerView.adapter = adapter
//
//        // ...
//    }
//}





