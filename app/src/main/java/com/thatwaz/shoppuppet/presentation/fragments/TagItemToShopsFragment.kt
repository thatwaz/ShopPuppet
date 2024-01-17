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
    private val tagItemToShopsViewModel: TagItemToShopsViewModel by viewModels()
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

        val newItem = navigationArgs.itemName
        Log.i("DOH!", "New item is $newItem")

        itemId = navigationArgs.itemId
        isPriority = navigationArgs.isPriority

        // Initializations
        initArguments()
        initViewModelData()

        // UI Setup
        setupPriorityIcon()
        setupRecyclerView()
        setupSaveButton()

        // Observers
        observeViewModelLiveData()
//        configureShopSelectionHandling()
        observeAndDisplayShops()



    }
// todo this ALMOST works but does not save with init shop selection plus priority icon is now showing when not clicked

    private fun configureShopSelectionHandling() {
        // Adjust the onItemClick to include the isChecked state
        shopSelectionAdapter.onItemClick = { selectedShop, isChecked ->

            // Log the interaction
            Log.i("ShopSelectionAdapter", "Shop item interacted: $selectedShop with checked state: $isChecked")
            tagItemToShopsViewModel.toggleShopSelection(selectedShop)
            // Use the isChecked boolean to add or remove the shop from the selection
            if (isChecked) {
                // If the item is checked, add the shop to the selection
                selectedShopsViewModel.addSelectedShop(selectedShop)
                Log.i("ShopSelection", "Shop selected (checked): $selectedShop")
            } else {
                // If the item is unchecked, remove the shop from the selection
                selectedShopsViewModel.removeSelectedShop(selectedShop)
                Log.i("ShopSelection", "Shop deselected (unchecked): $selectedShop")
            }
        }
    }

    private fun setupRecyclerView() {
        Log.d("AdapterLog", "Setting up RecyclerView with adapter")

        val recyclerView: RecyclerView = binding.rvShopsToTag
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter with the onItemClick implementation
        shopSelectionAdapter = ShopSelectionAdapter { shop, isChecked ->
            // Placeholder for the new onItemClick implementation
            // Actual handling will be set in configureShopSelectionHandling
        }

        // Set the adapter to the RecyclerView
        recyclerView.adapter = shopSelectionAdapter

        // Call to configure the item click handling
        configureShopSelectionHandling()
    }

    private fun observeAndDisplayShops() {
        // Assume fetchAndSetSelectedShops updates the LiveData with List<ShopWithSelection>
        tagItemToShopsViewModel.fetchAndSetSelectedShops(itemId)
        tagItemToShopsViewModel.selectedShopsLiveData.observe(viewLifecycleOwner) { shopsWithSelection ->
            // Submit the new list to the adapter
            shopSelectionAdapter.submitList(shopsWithSelection)
        }
    }
    private fun observeViewModelLiveData() {
        observeShopData()
        // Add any other LiveData observers here
    }


    // todo two different viewmodels here brett

    // todo between this and list fragment override methods are not updated in u.i. immediately
    private fun setupSaveButton() {
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
                        ), selectedShopIds,isPriority
                    ).await()
                    // Handle the case for new item insertion
//                    viewModel.updateItemPriority(newItemId, isPriority)
                    Log.i("Kangaroo","fragment reads $newItemId is $isPriority")


                } else {
                    Log.i("Ice Cream","This else block is firing")
                    // Update existing item
//                    itemViewModel.updatePriorityStatus(itemId,isPriority)
                    itemViewModel.updateItem(itemId, itemName, selectedShopIds, isPriority)
                    Log.i("Kangaroo","fragment in edit mode reads $itemId is $isPriority")
                    // Handle the case for updating an item


                    
                }
//                setupRecyclerView()
                // Common code after insertion or update
                findNavController()
                    .navigate(
                        TagItemToShopsFragmentDirections
                            .actionTagItemToShopsFragmentToListFragment()
                    )
            }
        }
    }

    private fun initArguments() {
        // Retrieve and handle any arguments or navigation parameters
        val itemName = arguments?.getString("itemName") ?: "Default Name"
        binding.tvItemName.setText(itemName)

        itemId = navigationArgs.itemId
        isPriority = navigationArgs.isPriority

        // Handle associated shop IDs
        val associatedShopIds = navigationArgs.associatedShopIds.toList()
        selectedShopsViewModel.setSelectedShopIds(associatedShopIds)
    }

    private fun initViewModelData() {
        tagItemToShopsViewModel.fetchAndSetSelectedShops(itemId)
    }

    private fun setupPriorityIcon() {
        updatePriorityStatus()
        binding.ivPriorityStar.setOnClickListener {
            // Toggle the priority status
            isPriority = !isPriority
            updatePriorityStatus()
        }
    }


    private fun updatePriorityStatus() {
        if (isPriority) {
            // Change icon to filled star and set tint color
            binding.ivPriorityStar.setImageResource(R.drawable.ic_star) // Replace with your filled star icon
            binding.ivPriorityStar.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            // Change the background of the tv_item_name here
            binding.tvItemName.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

        } else {
            // Change icon to star outline
            binding.ivPriorityStar.setImageResource(R.drawable.ic_star_outline) // Replace with your outline star icon
            binding.ivPriorityStar.setColorFilter(null) // Remove tint color

            // Reset the background of the tv_item_name here or set it to a default color
            binding.tvItemName.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.off_white)) // or whatever your default color is
        }
    }


    private fun observeShopData() {
        tagItemToShopsViewModel.shops.observe(viewLifecycleOwner) { shops ->
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







