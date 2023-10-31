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
class ListFragment : Fragment() {

        private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val adapter = ListAdapter()

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

//    private fun showShopListDialog() {
//        val itemName = binding.etShoppingItem.text.toString()
//
//        if (itemName.isNotBlank()) {
//            val shopNames = allShops.map { it.name }.toTypedArray()
//            val shopIds = allShops.map { it.id }.toLongArray()
//
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle("Select a Shop")
//                .setSingleChoiceItems(shopNames, -1) { dialog, which ->
//                    // Handle shop selection here
//                    val selectedShopId = shopIds[which]
//                    val newItem = Item(name = itemName, description = "")
//                    val taggedShopIds = listOf(selectedShopId)
//                    viewModel.insertItemWithShops(newItem, taggedShopIds)
//                    binding.etShoppingItem.text.clear()
//                    dialog.dismiss()
//                }
//                .setNegativeButton("Cancel") { dialog, _ ->
//                    // Handle the negative button click if needed
//                    dialog.dismiss()
//                }
//            val dialog = builder.create()
//            dialog.show()
//        }
//    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


