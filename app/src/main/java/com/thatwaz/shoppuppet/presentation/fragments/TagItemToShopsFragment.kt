package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.presentation.viewmodel.TagItemToShopsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TagItemToShopsFragment : Fragment(R.layout.fragment_tag_item_to_shops) {

    // ViewModel for handling data and logic
    private val viewModel: TagItemToShopsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemName = arguments?.getString("itemName") // Retrieve item name from arguments

        // Initialize RecyclerView or ListView with a list of shops
        val shopsRecyclerView = view.findViewById<RecyclerView>(R.id.shopsRecyclerView)
        val shopListAdapter = ShopListAdapter(/* pass shop data */)
        shopsRecyclerView.adapter = shopListAdapter

        val saveButton = view.findViewById<Button>(R.id.btnSave)
        saveButton.setOnClickListener {
            // Collect selected shops and associate them with the item
            val selectedShops = shopListAdapter.getSelectedShops()
            viewModel.tagItemToShops(itemName, selectedShops)

            // Navigate back to the previous fragment or appropriate destination
            findNavController().popBackStack()
        }
    }
}
