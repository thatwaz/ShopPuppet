package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.FragmentListBinding
import com.thatwaz.shoppuppet.domain.model.ItemUiModel
import com.thatwaz.shoppuppet.presentation.adapters.ListAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import com.thatwaz.shoppuppet.util.ResourceCache
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment : Fragment(), ListAdapter.ItemClickListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding cannot be accessed.")
    private lateinit var listAdapter: ListAdapter

    private val itemViewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resourceCache = ResourceCache(requireContext())
        listAdapter = ListAdapter(this, resourceCache)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemViewModel.refreshUiModels()
        itemViewModel.cleanUpOldSoftDeletedItems()
        setupSortingIconsClickListeners()
        setupRecyclerView()
        observeListData()


        binding.fabAddItem.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddItemFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvShoppingList
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = listAdapter
    }

    private fun observeListData() {
        itemViewModel.itemUiModels.observe(viewLifecycleOwner) { shopList ->
            if (shopList.isNotEmpty()) {
                // Update the adapter with the new list if it's not empty
                listAdapter.submitList(shopList)
                listAdapter.onListUpdated = {
                    // This code block will be executed after the list has been updated.
                    binding.rvShoppingList.scrollToPosition(0)
                }

            } else {
                listAdapter.submitList(emptyList())

            }
        }
    }

    private fun setupSortingIconsClickListeners() {
        binding.ivAlphabetical.setOnClickListener {
            itemViewModel.onAlphabeticalSortIconClicked()
        }
        binding.ivOrderOfEntry.setOnClickListener {
            itemViewModel.onOrderOfEntrySortIconClicked()
        }
    }


    override fun onResume() {
        super.onResume()
        itemViewModel.refreshUiModels()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onEditItem(item: ItemUiModel) {
        // Prepare arguments
        val itemId = item.itemId
        val itemName = item.itemName
        val isPriority = item.isPriorityItem
        val associatedShopIds = item.shopNames.map { it.id }.toLongArray()

        // Navigate to TagItemToShopsFragment
        val action = ListFragmentDirections.actionListFragmentToTagItemToShopsFragment(
            itemName = itemName,
            itemId = itemId,
            isPriority = isPriority,
            associatedShopIds = associatedShopIds
        )
        findNavController().navigate(action)
    }

    override fun onDeleteItem(item: ItemUiModel) {
        val itemToDelete = itemViewModel.findItemByUiModelForDeletion(item)

        if (itemToDelete != null) {
            // Show confirmation dialog before deletion
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Item") // Set the title of the dialog
                .setMessage("Are you sure you want to delete this item?") // Set the message
                .setPositiveButton("Delete") { _, _ ->
                    itemViewModel.hardDeleteItemWithShops(itemToDelete)
                }
                .setNegativeButton("Cancel", null) // Do nothing on cancel
                .show()
        } else {
            Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
        }
    }

}

