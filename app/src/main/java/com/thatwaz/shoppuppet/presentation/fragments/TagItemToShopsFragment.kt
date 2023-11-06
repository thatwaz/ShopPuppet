package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentTagItemToShopsBinding
import com.thatwaz.shoppuppet.domain.model.Item
import com.thatwaz.shoppuppet.presentation.adapters.ShopSelectionAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.SelectedShopsViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.TagItemToShopsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TagItemToShopsFragment() : Fragment(R.layout.fragment_tag_item_to_shops) {

    private var _binding: FragmentTagItemToShopsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TagItemToShopsViewModel by viewModels()
    private val selectedShopsViewModel: SelectedShopsViewModel by viewModels()
    private val itemViewModel: ItemViewModel by viewModels()
    private lateinit var shopSelectionAdapter: ShopSelectionAdapter

    private val navigationArgs: TagItemToShopsFragmentArgs by navArgs()

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

        // Retrieve the item name from fragment arguments
        val itemName = arguments?.getString("itemName")

        // Set the item name in your TextView
        binding.tvItemName.text = itemName

        val newItem = navigationArgs.itemName
        Log.i("DOH!", "New item is $newItem")

        setupRecyclerView()
        observeShopData()

        shopSelectionAdapter.onItemClick = { selectedShop ->
            selectedShopsViewModel.addSelectedShop(selectedShop)

            binding.btnSave.setOnClickListener {
                val newItemName = Item(name = newItem, description = "")
                val selectedShopIds =
                    selectedShopsViewModel.selectedShops.value?.map { it.id } ?: emptyList()
                itemViewModel.insertItemWithShops(newItemName, selectedShopIds)
                val action = TagItemToShopsFragmentDirections
                    .actionTagItemToShopsFragmentToListFragment()
                findNavController().navigate(action)

            }

        }
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
//class TagItemToShopsFragment() : Fragment(R.layout.fragment_tag_item_to_shops) {
//
//    private var _binding: FragmentTagItemToShopsBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel: TagItemToShopsViewModel by viewModels()
//    private val selectedShopsViewModel: SelectedShopsViewModel by viewModels()
//    private val itemViewModel: ItemViewModel by viewModels()
//    private lateinit var shopSelectionAdapter: ShopSelectionAdapter
//
//
//
//    private val navigationArgs: TagItemToShopsFragmentArgs by navArgs()
//
////
////    val args = TagItemToShopsFragmentArgs.fromBundle(requireArguments())
////    val itemName = args.itemName
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentTagItemToShopsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    private fun bind(item: Item) {
//        binding.tvItemName.text  = item.name
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        Log.d("TagItemToShopsFragment", "onViewCreated called")
//
//        var newItem = navigationArgs.itemName
//        Log.i("DOH!","New item is $newItem")
//
//
//        binding.tvItemName.text = newItem
//
//        setupRecyclerView()
//        observeShopData()
//
//        shopSelectionAdapter.onItemClick = { selectedShop ->
//            selectedShopsViewModel.addSelectedShop(selectedShop)
//
//            binding.btnSave.setOnClickListener {
////                newItem = binding.tvItemName.text.toString()
//
//                val itemName = Item(name = newItem, description = "")
////                val itemDescription = binding.etItemDescription.text.toString()
//                val selectedShopIds =
//                    selectedShopsViewModel.selectedShops.value?.map { it.id } ?: emptyList()
//
////                if (itemName.isNotBlank() && selectedShopIds.isNotEmpty()) {
//                // Call the ViewModel function to save the item with tagged shops
//                itemViewModel.insertItemWithShops(itemName, selectedShopIds)
//
//                // Clear the input fields and any other necessary UI updates
////                    binding.tvItemName.text.clear()
////                    selectedShopsViewModel.clearSelectedShops()
////                } else {
////                    // Handle cases where item name is blank or no shops are selected
////                    // You can show an error message to the user
////                }
////            }
//            }
//        }
//
//        // ... (Your existing code)
//    }
//
//
//
//
//    private fun setupRecyclerView() {
//        val recyclerView: RecyclerView = binding.rvShopsToTag
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        shopSelectionAdapter = ShopSelectionAdapter(
//            onItemClick = { selectedShop ->
//                selectedShopsViewModel.addSelectedShop(selectedShop)
//            },
//            selectedShopsViewModel = selectedShopsViewModel
//        )
//        recyclerView.adapter = shopSelectionAdapter
//    }
//
//    private fun observeShopData() {
//        viewModel.shops.observe(viewLifecycleOwner) { shops ->
//            Log.d("DOH!", "Shops are this $shops")
//            shopSelectionAdapter.submitList(shops)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


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





