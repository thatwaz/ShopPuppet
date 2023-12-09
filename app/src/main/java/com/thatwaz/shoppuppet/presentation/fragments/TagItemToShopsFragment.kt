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

        Log.d("TagItemToShopsFragment", "onViewCreated called")

        // Retrieve the item name from fragment arguments
        val itemName = arguments?.getString("itemName")

        // Set the item name in your TextView
        binding.tvItemName.text = itemName

        val newItem = navigationArgs.itemName
        Log.i("DOH!", "New item is $newItem")

//        val itemId = navigationArgs.itemId // Assuming you pass the item ID via navigation args
        binding.ivPriorityStar.setOnClickListener {
            // Toggle the priority status
            isPriority = !isPriority
            updatePriorityIcon()
        }

//        binding.ivPriorityStar.setOnClickListener {
//            // Toggle the priority status
//            Log.i("DebugLog", "Priority star clicked")
//            isPriority = !isPriority
//            updatePriorityIcon()
//
//            // Update the priority status in the ViewModel
//            if (itemId != -1L) {
//                viewModel.updateItemPriority(itemId, isPriority)

//            }
//        }
//        binding.ivPriorityStar.setOnClickListener {
//            // Toggle the priority status
//            isPriority = !isPriority
//            updatePriorityIcon()
//
//            // Update the priority status in the ViewModel
//            viewModel.updateItemPriority(itemId, isPriority)
//            Log.i("Flanders", "Item name $itemName is now priority: $isPriority")
//        }

        setupRecyclerView()
        observeShopData()

        shopSelectionAdapter.onItemClick = { selectedShop ->
            if (selectedShopsViewModel.isSelected(selectedShop)) {
                selectedShopsViewModel.addSelectedShop(selectedShop)
            } else {
                selectedShopsViewModel.removeSelectedShop(selectedShop)
            }
            Log.i("ShopSelection", "Selected shops updated")
            Log.i("Goose", "sel shop is $selectedShop")
        }



        binding.btnSave.setOnClickListener {
            val selectedShopIds = selectedShopsViewModel.selectedShops.value?.map { it.id } ?: emptyList()

            if (selectedShopIds.isEmpty()) {
                Toast.makeText(context, "Please select at least one shop", Toast.LENGTH_SHORT).show()
            } else {
                val newItemName = Item(name = navigationArgs.itemName, description = "")

                lifecycleScope.launch {
                    itemId = itemViewModel.insertItemWithShopsAsync(newItemName, selectedShopIds).await()
                    if (itemId != -1L) {
                        Log.i("DebugLog", "Item inserted with ID: $itemId")
                Log.i("Flanders", "Item ID $itemId is now priority: $isPriority")
                        // Update the item's priority status using the stored 'isPriority' value
                        viewModel.updateItemPriority(itemId, isPriority)

                        // Navigate or perform other actions
                        val action = TagItemToShopsFragmentDirections.actionTagItemToShopsFragmentToListFragment()
                        findNavController().navigate(action)
                    } else {
                        // Handle the case where item insertion failed
                        Log.e("DebugLog", "Failed to insert the item")
                    }
                }
            }
        }


//        binding.btnSave.setOnClickListener {
//            val selectedShopIds = selectedShopsViewModel.selectedShops.value?.map { it.id } ?: emptyList()
//
//            if (selectedShopIds.isEmpty()) {
//                Toast.makeText(context, "Please select at least one shop", Toast.LENGTH_SHORT).show()
//            } else {
//                val newItemName = Item(name = navigationArgs.itemName, description = "")
//
//                lifecycleScope.launch {
//                    itemId = itemViewModel.insertItemWithShopsAsync(newItemName, selectedShopIds).await()
//                    if (itemId != -1L) {
//                        Log.i("DebugLog", "Item inserted with ID: $itemId")
//                        // Navigate or perform other actions
//                        val action = TagItemToShopsFragmentDirections.actionTagItemToShopsFragmentToListFragment()
//                        findNavController().navigate(action)
//                    } else {
//                        // Handle the case where item insertion failed
//                        Log.e("DebugLog", "Failed to insert the item")
//                    }
//                }
//            }
//        }

//        binding.btnSave.setOnClickListener {
//            val selectedShopIds = selectedShopsViewModel.selectedShops.value?.map { it.id } ?: emptyList()
//
//            if (selectedShopIds.isEmpty()) {
//                // Show a Toast message if no shops are selected
//                Toast.makeText(context, "Please select at least one shop", Toast.LENGTH_SHORT).show()
//            } else {
//                // Continue with the saving process
//                val newItemName = Item(name = newItem, description = "")
//
//                lifecycleScope.launch {
//                    itemViewModel.insertItemWithShopsAsync(newItemName, selectedShopIds).await()
//                    Log.i("DebugLog", "Item inserted with ID: $itemId")
//                    // Navigate back to ListFragment
//                    val action = TagItemToShopsFragmentDirections.actionTagItemToShopsFragmentToListFragment()
//                    findNavController().navigate(action)
//                }
//            }
//        }

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
            shopSelectionAdapter.submitList(shops)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}







