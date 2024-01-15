package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.FragmentAddItemBinding
import com.thatwaz.shoppuppet.domain.model.Item
import com.thatwaz.shoppuppet.presentation.adapters.RecentlyPurchasedItemAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopSpecificListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by viewModels()
    private val shopSpecificListViewModel: ShopSpecificListViewModel by viewModels()

    private lateinit var adapter: RecentlyPurchasedItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        shopSpecificListViewModel.purchasedAndSoftDeletedItems.observe(viewLifecycleOwner) { items ->
            items.forEach { item ->
                Log.i("FrequentItemLog", "Item: ${item.name}, Last Purchased: ${item.lastPurchasedDate}")
            }
            adapter.submitList(items)
        }

        binding.btnNext.setOnClickListener {
            navigateToNextFragment()
        }

        binding.fabTempDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        adapter.onItemLongPress = { item ->
            // Handle the long press event, e.g., show a confirmation dialog for deletion
            showSingleItemDeleteConfirmationDialog(item)
        }


        binding.etItemName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val filterText = s.toString()
                shopSpecificListViewModel.purchasedAndSoftDeletedItems.value?.let { items ->
                    val filteredItems = if (filterText.isEmpty()) {
                        items
                    } else {
                        items.filter { it.name.startsWith(filterText, ignoreCase = true) }
                    }
                    adapter.submitList(filteredItems)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for filtering
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for filtering
            }
        })
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvFrequentlyPurchasedItems
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RecentlyPurchasedItemAdapter().apply {
            onItemClick = { item ->
                binding.etItemName.setText(item.name)
            }
        }
        recyclerView.adapter = adapter
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(context)
            .setTitle("Delete Soft Deleted Items")
            .setMessage("Are you sure you want to permanently delete all recently purchased items?")
            .setPositiveButton("Delete") { dialog, which ->
                // Call the ViewModel function to delete soft-deleted items
                shopSpecificListViewModel.hardDeleteSoftDeletedItems()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSingleItemDeleteConfirmationDialog(item: Item) {
        AlertDialog.Builder(context)
            .setTitle("Delete Recently Purchased Item")
            .setMessage("Are you sure you want to permanently delete this item?")
            .setPositiveButton("Delete") { dialog, which ->

                viewModel.deleteItemWithShops(item)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



    private fun navigateToNextFragment() {
        val itemName = binding.etItemName.text.toString()
        viewModel.updateItemName(itemName)


        // Set default values for other arguments
        val itemId = -1L // Default ID for new item
        val isPriority = false // Default priority status for new item
        val associatedShopIds = longArrayOf() // Empty array for new item

        // Navigate to the next fragment with all arguments
        val action = AddItemFragmentDirections.actionAddItemFragmentToTagItemToShopsFragment(
            itemName = itemName,
            itemId = itemId,
            isPriority = isPriority,
            associatedShopIds = associatedShopIds
        )
        findNavController().navigate(action)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




