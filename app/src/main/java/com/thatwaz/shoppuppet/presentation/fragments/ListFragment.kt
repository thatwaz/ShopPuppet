package com.thatwaz.shoppuppet.presentation.fragments

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
import com.thatwaz.shoppuppet.databinding.FragmentListBinding
import com.thatwaz.shoppuppet.domain.model.ItemUiModel
import com.thatwaz.shoppuppet.presentation.adapters.ListAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment : Fragment(), ListAdapter.ItemClickListener  {

        private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
//    private val adapter = ListAdapter(this)
    private lateinit var listAdapter: ListAdapter

    // Injecting the ViewModel
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


    private fun observeListData() {
        viewModel.itemUiModels.observe(viewLifecycleOwner) { shopList ->
            if (shopList.isNotEmpty()) {
                // Update the adapter with the new list if it's not empty
                listAdapter.submitList(shopList)
                Log.i("Crispy", "Shoplist is $shopList")
            } else {
                // Handle the empty list case
                // Update the adapter with an empty list to clear the RecyclerView
                listAdapter.submitList(emptyList())

                // Optionally, display a message or a view indicating the list is empty
                // For example, show an empty state view or hide certain UI elements
                // (Assuming you have an 'emptyListView' or similar in your layout)
//                binding.emptyListView.visibility = View.VISIBLE

                Log.i("Crispy", "Shoplist is empty")
            }
        }
    }


//    private fun observeListData() {
//        viewModel.itemUiModels.observe(viewLifecycleOwner) { shopList ->
//            // Check if shopList is not empty or null (based on your logic)
//            if (shopList.isNotEmpty()) {
//                listAdapter.submitList(shopList)
//                Log.i("Crispy", "Shoplist is $shopList")
//            }
//        }
//    }

//    private fun observeListData() {
//        viewModel.itemUiModels.observe(viewLifecycleOwner) { shopList ->
//            listAdapter.submitList(shopList)
//            Log.i("Crispy","shoplist is $shopList")
//        }
//    }

//    override fun onResume() {
//        super.onResume()
//        Log.i("Crispy","onResume called")
//        viewModel.refreshData()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteItem(itemUiModel: ItemUiModel) {
        // Find the corresponding Item object from the ViewModel's data
        val item = viewModel.findItemByUiModel(itemUiModel)

        if (item != null) {
            // Call the deleteItemWithShops function with the Item object
            viewModel.deleteItemWithShops(item)

        } else {
            // Handle the case where the corresponding Item object is not found
        }

    }

}

