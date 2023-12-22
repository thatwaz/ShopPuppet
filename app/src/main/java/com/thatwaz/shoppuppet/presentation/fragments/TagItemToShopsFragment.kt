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

        // Set the item name in your TextView
        // changed to edit text to handle name edit
        binding.tvItemName.setText(itemName)

        val newItem = navigationArgs.itemName
        Log.i("DOH!", "New item is $newItem")

        viewModel.fetchAndSetSelectedShops(itemId)




        itemId = navigationArgs.itemId
        isPriority = navigationArgs.isPriority

        if (itemId != -1L) { // If editing an existing item
            viewModel.fetchAndSetSelectedShops(itemId)
            viewModel.selectedShops.observe(viewLifecycleOwner) { shops ->
                // Initialize the SelectedShopsViewModel with these shops
                val selectedShopIds = shops.map { it.id }
                selectedShopsViewModel.setSelectedShopIds(selectedShopIds)
                selectedShopsViewModel.initializeSelectedShops(shops)
            }
//            viewModel.selectedShops.observe(viewLifecycleOwner) { shops ->
//                // Now synchronize this data to SelectedShopsViewModel
//
//            }
        }


        viewModel.selectedShops.observe(viewLifecycleOwner) { selectedShops ->
            Log.i("Crappy", "Selected Shops are $selectedShops")

            // Assuming viewModel.shops holds the full list of shops
            viewModel.shops.value?.let { fullShopsList ->
                // Create a Set of selected Shop IDs for faster lookup
                val selectedShopIds = selectedShops.map { it.id }.toSet()

                // Transform the full list of shops into a list of ShopWithSelection
                val shopsWithSelection = fullShopsList.map { shop ->
                    ShopWithSelection(
                        shop = shop,
                        isSelected = selectedShopIds.contains(shop.id)
                    )
                }

                // Submit the updated list to the adapter
                (binding.rvShopsToTag.adapter as? ShopSelectionAdapter)?.submitList(
                    shopsWithSelection
                )
            }
        }

        updatePriorityIcon()

        binding.ivPriorityStar.setOnClickListener {
            // Toggle the priority status
            isPriority = !isPriority
            updatePriorityIcon()
        }

        setupRecyclerView()
        observeShopData()


        //todo This might have something to do with the log showing added and removed at the same time
        shopSelectionAdapter.onItemClick = { selectedShop ->
            // Correctly toggle the selection state
            if (selectedShopsViewModel.isSelected(selectedShop)) {
                // If the shop is already selected, remove it from the selection
                selectedShopsViewModel.removeSelectedShop(selectedShop)
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
            binding.ivPriorityStar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
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
            val selectedShopIds = selectedShopsViewModel.selectedShops.value?.map { it.id }?.toSet() ?: emptySet()

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







