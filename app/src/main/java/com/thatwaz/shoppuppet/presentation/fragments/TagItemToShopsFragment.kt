package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentTagItemToShopsBinding
import com.thatwaz.shoppuppet.domain.model.Item
import com.thatwaz.shoppuppet.domain.model.ShopWithSelection
import com.thatwaz.shoppuppet.presentation.adapters.ShopSelectionAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.SelectedShopsViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.TagItemToShopsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TagItemToShopsFragment() : Fragment() {

    private var _binding: FragmentTagItemToShopsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TagItemToShopsViewModel by viewModels()
    private val selectedShopsViewModel: SelectedShopsViewModel by viewModels()
    private val itemViewModel: ItemViewModel by viewModels()

    //    private val tagItemToShopsViewModel: TagItemToShopsViewModel by viewModels()
    private lateinit var shopSelectionAdapter: ShopSelectionAdapter
    private var isPriority = false
    private var itemId: Long = -1

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


        // Retrieve the item name from fragment arguments
        val itemName = arguments?.getString("itemName")
        // Retrieve associated shop IDs from navigation arguments
        val associatedShopIds: LongArray =
            navigationArgs.associatedShopIds // Assuming this is LongArray
        fetchAndInitializeSelectedShops(associatedShopIds.toList()) // Convert to List<Long> when passing


        selectedShopsViewModel.setSelectedShopIds(associatedShopIds.toList())  // Ensure this method exists and updates the ViewModel's LiveData


        // Set the item name in your TextView
        // changed to edit text to handle name edit
        binding.tvItemName.setText(itemName)

        val newItem = navigationArgs.itemName
        Log.i("DOH!", "New item is $newItem")

        itemId = navigationArgs.itemId
        isPriority = navigationArgs.isPriority
        viewModel.fetchAndSetSelectedShops(itemId)
        // Observe the full list of shops
//        viewModel.shops.observe(viewLifecycleOwner) { allShops ->
//            combineAndSubmit(allShops, selectedShopsViewModel.selectedShops.value)
//        }

        // Temporary test list of shops


// Observe the selected shops
//        selectedShopsViewModel.selectedShops.observe(viewLifecycleOwner) { selectedShops ->
//            selectedShops
////            combineAndSubmit(viewModel.shops.value, selectedShops)
//            Log.i("horseshit","shoppies are ${selectedShops}")
//            Log.i("AdapterDebug", "Submitting list to adapter: $selectedShops")
//            shopSelectionAdapter.submitList(selectedShops)
//        }


        updatePriorityIcon()

        binding.ivPriorityStar.setOnClickListener {
            // Toggle the priority status
            isPriority = !isPriority
            updatePriorityIcon()
        }

        setupRecyclerView()
        observeShopData()

//        selectedShopsViewModel.selectedShops.observe(viewLifecycleOwner) { selectedShops ->

        if (itemId != -1L) {
            viewModel.selectedShops.observe(viewLifecycleOwner) { selectedShops ->


                Log.i("horseshit", "shoppies are ${selectedShops}")
                // Assuming viewModel.shops holds the full list of shops
                viewModel.allShopsLiveData.value?.let { allShops ->
                    Log.i("horseshit", "Current viewModel.shops: ${viewModel.allShopsLiveData.value}")

                    // Transform the list of all shops into a list of ShopWithSelection
                    // where each shop's selection status is determined by whether it's in the selectedShops list

                    val shopsWithSelection = allShops.map { shop ->
                        ShopWithSelection(
                            shop = shop,
                            isSelected = selectedShops.contains(shop)
                        )
                    }
                    // Log the transformed list
                    Log.i("horseshit", "ShopWithSelection list: $shopsWithSelection")
//                // Now submit this transformed list to your adapter

                    /*todo viewmodel.loadShops() brings shops back but not cheked, do I need a
                    similar function in viewmodel to reload shops minus the checkbox?*/
//                    viewModel.loadShops()
                    shopSelectionAdapter.submitList(shopsWithSelection)
                }
            }
        }

        //todo This might have something to do with the log showing added and removed at the same time
        shopSelectionAdapter.onItemClick = { selectedShop ->
            // Correctly toggle the selection state
            if (selectedShopsViewModel.isSelected(selectedShop)) {
                // If the shop is already selected, remove it from the selection
//                selectedShopsViewModel.removeSelectedShop(selectedShop)
            } else {
                // If the shop is not selected, add it to the selection
                selectedShopsViewModel.addSelectedShop(selectedShop)
            }
            Log.i(
                "ShopSelection",
                "Selected shops updated: ${selectedShopsViewModel.selectedShops.value}"
            )
            Log.i("Goose", "Selected shop is $selectedShop")
        }

        // Assuming selectedShopsLiveData holds a list of ShopWithSelection
        viewModel.selectedShopsLiveData.observe(viewLifecycleOwner) { shopsWithSelection ->
            // Submit the new list to the adapter
            shopSelectionAdapter.submitList(shopsWithSelection)
        }

        binding.btnSave.setOnClickListener {
            val itemName = binding.tvItemName.text.toString()
            val selectedShopIds =
                selectedShopsViewModel.selectedShops.value?.map { it.id } ?: emptyList()

            if (selectedShopIds.isEmpty()) {
                Toast.makeText(context, "Please select at least one shop", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (itemId == -1L) {
                    // Add a new item
                    val newItemId = itemViewModel.insertItemWithShopsAsync(
                        Item(
                            name = itemName,
                            description = ""
                        ), selectedShopIds
                    ).await()
                    // Handle the case for new item insertion
                    viewModel.updateItemPriority(newItemId, isPriority)

                } else {
                    // Update existing item
                    itemViewModel.updateItem(itemId, itemName, selectedShopIds, isPriority)
                    // Handle the case for updating an item
                }

                // Common code after insertion or update
                findNavController().navigate(TagItemToShopsFragmentDirections.actionTagItemToShopsFragmentToListFragment())
            }
        }

    }

    private fun fetchAndInitializeSelectedShops(shopIds: List<Long>) {
        // Assuming you have a method in your ViewModel to fetch shops by IDs
        viewModel.fetchShopsByIds(shopIds).observe(viewLifecycleOwner) { shops ->
            // Now that you have the shops, initialize the SelectedShopsViewModel
            selectedShopsViewModel.initializeSelectedShops(shops)
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

    private fun updatePriorityIcon() {
        if (isPriority) {
            // Change icon to filled star and set tint color
            binding.ivPriorityStar.setImageResource(R.drawable.ic_star) // Replace with your filled star icon
            binding.ivPriorityStar.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            binding.ivStarPriorityMarker.visibility = View.VISIBLE
        } else {
            // Change icon to star outline
            binding.ivPriorityStar.setImageResource(R.drawable.ic_star_outline) // Replace with your outline star icon
            binding.ivPriorityStar.setColorFilter(null) // Remove tint color
            binding.ivStarPriorityMarker.visibility = View.INVISIBLE
        }
    }

    private fun observeShopData() {
        viewModel.shops.observe(viewLifecycleOwner) { shops ->
            Log.d("DOH!", "Shops are this $shops")

            // Assuming selectedShopsViewModel holds the current selection state
            val selectedShopIds =
                selectedShopsViewModel.selectedShops.value?.map { it.id }?.toSet() ?: emptySet()

            // Transform the list of Shops to a list of ShopWithSelection
            val shopsWithSelection = shops.map { shop ->
                ShopWithSelection(
                    shop = shop,
                    isSelected = selectedShopIds.contains(shop.id)
                )
            }

            // Submit the transformed list to the adapter
            shopSelectionAdapter.submitList(shopsWithSelection)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}







