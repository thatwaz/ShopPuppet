package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.thatwaz.shoppuppet.databinding.FragmentListBinding
import com.thatwaz.shoppuppet.domain.model.Item
import com.thatwaz.shoppuppet.presentation.adapters.ListAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListAdapter

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


        adapter = ListAdapter(listOf(), emptyMap())
        binding.rvShoppingList.adapter = adapter
        binding.rvShoppingList.layoutManager = LinearLayoutManager(context)

        viewModel.shops.observe(viewLifecycleOwner, { shops ->
            // You can handle the data here if needed.
            // For now, this will ensure that the shops LiveData is actively being observed and updated.
        })


        // Observe LiveData from ViewModel to update UI
        viewModel.items.observe(viewLifecycleOwner, { items ->
            adapter.updateData(items, emptyMap())
        })

//        viewModel.items.observe(viewLifecycleOwner, { items ->
//            adapter.updateData(items)
//        })

        binding.btnAddItem.setOnClickListener {
            val itemName = binding.etShoppingItem.text.toString()

            if (itemName.isNotBlank()) {
                // Fetch shops from database (this should ideally be loaded once, not every time you click on the button)
                val shops = viewModel.shops.value ?: listOf()
                val shopNames = shops.map { it.name }.toTypedArray()
                val checkedItems = BooleanArray(shopNames.size)

                AlertDialog.Builder(requireContext())
                    .setTitle("Tag to shops")
                    .setMultiChoiceItems(shopNames, checkedItems) { _, which, isChecked ->
                        checkedItems[which] = isChecked
                    }
                    .setPositiveButton("OK") { _, _ ->
                        val taggedShopIds = shops.filterIndexed { index, _ -> checkedItems[index] }.map { it.id }
                        // Assuming description is empty for simplicity
                        val newItem = Item(name = itemName, description = "")
                        viewModel.insertItemWithShops(newItem, taggedShopIds)  // Save to database and associate with shops
                        binding.etShoppingItem.text.clear()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
