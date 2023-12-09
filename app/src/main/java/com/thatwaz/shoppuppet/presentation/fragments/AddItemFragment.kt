package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thatwaz.shoppuppet.databinding.FragmentAddItemBinding
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val itemName = binding.etItemName.text.toString()
            viewModel.updateItemName(itemName)

            // Navigate to the next fragment with item name as an argument
            val action = AddItemFragmentDirections.actionAddItemFragmentToTagItemToShopsFragment(itemName)
            findNavController().navigate(action)
        }


//        binding.btnNext.setOnClickListener {
//            val itemName = binding.etItemName.text.toString()
//            viewModel.updateItemName(itemName)
//
//            // Use lifecycleScope for fragment coroutine
//            lifecycleScope.launch {
//                val itemId = viewModel.insertNewItem(itemName)
//                val action = AddItemFragmentDirections.actionAddItemFragmentToTagItemToShopsFragment(itemName, itemId)
//                findNavController().navigate(action)
//            }
//        }



//        binding.btnNext.setOnClickListener {
//            val itemName = binding.etItemName.text.toString()
//            Log.i("DOH!", "Item name from EditText: $itemName")
//            val itemId = 1L
//            // Update the ViewModel with the item name
//            viewModel.updateItemName(itemName)
//
//            // Navigate to the next fragment with item name as an argument
////            val bundle = bundleOf("itemName" to itemName)
////            findNavController().navigate(R.id.action_addItemFragment_to_tagItemToShopsFragment, bundle)
//            val action = AddItemFragmentDirections.actionAddItemFragmentToTagItemToShopsFragment(itemName, itemId)
//            findNavController().navigate(action)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

