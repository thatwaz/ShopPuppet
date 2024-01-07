package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment : Fragment(), ListAdapter.ItemClickListener  {

        private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ListAdapter

    private val viewModel: ItemViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.logItemsWithAssociatedShops()
        setupRecyclerView()
        observeListData()


        binding.fabAddItem.setOnClickListener {
            val action = ListFragmentDirections
                .actionListFragmentToAddItemFragment()
            findNavController().navigate(action)

        }
    }
    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvShoppingList
        recyclerView.layoutManager = LinearLayoutManager(context)
        listAdapter = ListAdapter(this)
        recyclerView.adapter = listAdapter
    }

// todo TRY TO REMOVE IF ELSE STATEMENT

    // TODO NEED TO UPDATE SHOPLIST TO ONLY SHOW UNPURCHASED ITEMS
    private fun observeListData() {
        viewModel.itemUiModels.observe(viewLifecycleOwner) { shopList ->
            if (shopList.isNotEmpty()) {
                // Update the adapter with the new list if it's not empty
                listAdapter.submitList(shopList)
                Log.i("Crispy", "Shoplist is $shopList")
            } else {
                listAdapter.submitList(emptyList())
                Log.i("Crispy", "Shoplist is empty")
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // todo why is this an override method?

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

    override fun onDeleteItem(itemUiModel: ItemUiModel) {
        val item = viewModel.findItemByUiModel(itemUiModel)

        if (item != null) {
            // Show confirmation dialog before deletion
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Item") // Set the title of the dialog
                .setMessage("Are you sure you want to delete this item?") // Set the message
                .setPositiveButton("Delete") { dialog, which ->
                    // Call the delete function when user confirms
                    viewModel.deleteItemWithShops(item)
                }
                .setNegativeButton("Cancel", null) // Do nothing on cancel
                .show()
        } else {
            // Handle the case where the corresponding Item object is not found
            Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
        }
    }



//    override fun onDeleteItem(itemUiModel: ItemUiModel) {
//        // Find the corresponding Item object from the ViewModel's data
//        val item = viewModel.findItemByUiModel(itemUiModel)
//
//        if (item != null) {
//            // Call the deleteItemWithShops function with the Item object
//            viewModel.deleteItemWithShops(item)
//
//        } else {
//            // Handle the case where the corresponding Item object is not found
//        }
//
//    }

}

