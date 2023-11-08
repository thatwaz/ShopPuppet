package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thatwaz.shoppuppet.databinding.FragmentListBinding
import com.thatwaz.shoppuppet.domain.model.ItemUiModel
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.presentation.adapters.ListAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class ListFragment : Fragment(), ListAdapter.ItemClickListener  {

        private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val adapter = ListAdapter(this)

    // Injecting the ViewModel
    private val viewModel: ItemViewModel by viewModels()

    private var allShops: List<Shop> = emptyList()

    private val itemListLiveData = MutableLiveData<List<ItemUiModel>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

                itemListLiveData.observe(viewLifecycleOwner) { itemList ->
            // Update your RecyclerView adapter with the latest data
            adapter.submitList(itemList)
        }

        viewModel.itemUiModels.observe(viewLifecycleOwner) { itemUiModels ->
            adapter.submitList(itemUiModels)
        }

        viewModel.shops.observe(viewLifecycleOwner) { shops ->
            // Update the allShops variable here
            allShops = shops
        }
//        }

        viewModel.fetchAllItems()
        viewModel.logItemsWithAssociatedShops()
//
//        adapter = ListAdapter(listOf())
        binding.rvShoppingList.adapter = adapter
        binding.rvShoppingList.layoutManager = LinearLayoutManager(context)


        binding.fabAddItem.setOnClickListener {
            val action = ListFragmentDirections
                .actionListFragmentToAddItemFragment()
            findNavController().navigate(action)
//            showShopListDialog()
        }


    }




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


//    override fun onDeleteItem(item: ItemUiModel) {
//        viewModel.deleteItemWithShops(item)
//    }
}

