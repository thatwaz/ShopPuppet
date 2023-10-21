package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thatwaz.shoppuppet.databinding.FragmentListBinding
import com.thatwaz.shoppuppet.domain.model.ShoppingItem
import com.thatwaz.shoppuppet.presentation.adapters.ListAdapter


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListAdapter

    private val itemList = mutableListOf<ShoppingItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter(itemList) // use the member variable
        binding.rvShoppingList.adapter = adapter
        binding.rvShoppingList.layoutManager = LinearLayoutManager(context)


        binding.btnAddItem.setOnClickListener {
            val itemName = binding.etShoppingItem.text.toString()

            if (itemName.isNotBlank()) {
                // Fetch shops from database (this is just a mock, replace with actual fetching from Room)
                val shopsFromDatabase = arrayOf("Winn-Dixie",
                    "Target",
                    "Walgreens",
                    "Bed Bath and Beyond")
                val checkedItems = BooleanArray(shopsFromDatabase.size)

                AlertDialog.Builder(requireContext())
                    .setTitle("Tag to shops")
                    .setMultiChoiceItems(shopsFromDatabase, checkedItems) { _, which, isChecked ->
                        checkedItems[which] = isChecked
                    }
                    .setPositiveButton("OK") { _, _ ->
                        val taggedShops = shopsFromDatabase.filterIndexed { index, _ -> checkedItems[index] }
                        val newItem = ShoppingItem(itemName, taggedShops)
                        itemList.add(newItem)
                        adapter.updateData(itemList)
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