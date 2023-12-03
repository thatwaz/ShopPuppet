package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                // Show a Toast message if no shops are selected
                Toast.makeText(context, "Please select at least one shop", Toast.LENGTH_SHORT).show()
            } else {
                // Continue with the saving process
                val newItemName = Item(name = newItem, description = "")

                lifecycleScope.launch {
                    itemViewModel.insertItemWithShopsAsync(newItemName, selectedShopIds).await()

                    // Navigate back to ListFragment
                    val action = TagItemToShopsFragmentDirections.actionTagItemToShopsFragmentToListFragment()
                    findNavController().navigate(action)
                }
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







