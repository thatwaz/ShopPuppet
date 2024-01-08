package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
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
import com.thatwaz.shoppuppet.presentation.adapters.FrequentlyPurchasedItemAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopSpecificListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by viewModels()
    private val shopSpecificListViewModel: ShopSpecificListViewModel by viewModels()

    private lateinit var adapter: FrequentlyPurchasedItemAdapter

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

        // Set the list for your adapter
        // Todo maybe just utilize this as recently purchased items and delete after 30 days and Massive code clean up

        shopSpecificListViewModel.purchasedAndSoftDeletedItems.observe(viewLifecycleOwner) { items ->
            items.forEach { item ->
                Log.i("FrequentItemLog", "Item: ${item.name}, Last Purchased: ${item.lastPurchasedDate}")
            }
            adapter.submitList(items)
        }


//        viewModel.frequentItems.observe(viewLifecycleOwner) { items ->
//            Log.i("AddItemFragment","Freq items are $items")
//            adapter.submitList(items)
//        }



        binding.btnNext.setOnClickListener {
            navigateToNextFragment()
        }

        binding.fabTempDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvFrequentlyPurchasedItems
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FrequentlyPurchasedItemAdapter().apply {
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

        // ... rest of your code for navigation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//@AndroidEntryPoint
//class AddItemFragment : Fragment() {
//
//    private var _binding: FragmentAddItemBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: ItemViewModel by viewModels()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.btnNext.setOnClickListener {
//            val itemName = binding.etItemName.text.toString()
//            viewModel.updateItemName(itemName)
//
//            // Set default values for other arguments
//            val itemId = -1L // Default ID for new item
//            val isPriority = false // Default priority status for new item
//            val associatedShopIds = longArrayOf() // Empty array for new item
//
//            // Navigate to the next fragment with all arguments
//            val action = AddItemFragmentDirections.actionAddItemFragmentToTagItemToShopsFragment(
//                itemName = itemName,
//                itemId = itemId,
//                isPriority = isPriority,
//                associatedShopIds = associatedShopIds
//            )
//            findNavController().navigate(action)
//        }
//
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

