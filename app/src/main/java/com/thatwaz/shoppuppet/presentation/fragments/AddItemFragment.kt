package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentAddItemBinding
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by viewModels()

    private val navigationArgs: TagItemToShopsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe changes in item name LiveData and update the EditText

        // Retrieve the item name from Safe Args
//        val itemName = "Jello"
//        Log.i("DOH!", "Item name from Safe Args: $itemName")

//        val itemName = binding.etItemName.text.toString()


        // Set the item name in your EditText
//        binding.etItemName.setText(itemName)

        binding.btnNext.setOnClickListener {
            val itemName = binding.etItemName.text.toString()
            Log.i("DOH!", "Item name from EditText: $itemName")
            // Update the ViewModel with the item name
            viewModel.updateItemName(itemName)


//            viewModel.getItemNameLiveData().observe(viewLifecycleOwner) { item ->
//                Log.i("DOH!", "LiveData updated with item: $item")
//                item?.let {
//                    binding.etItemName.setText(it)
//                }
//            }

            // Navigate to the next fragment with item name as an argument
            val bundle = bundleOf("itemName" to itemName)
            findNavController().navigate(R.id.action_addItemFragment_to_tagItemToShopsFragment, bundle)
        }

//        binding.btnNext.setOnClickListener {
//            viewModel.updateItemName(itemName.toString())
//
//            val action = AddItemFragmentDirections
//                .actionAddItemFragmentToTagItemToShopsFragment(itemName.toString())
//            Log.i("DOH!"," Item name in add item is $itemName")
//            findNavController().navigate(action)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}






//
//@AndroidEntryPoint
//class AddItemFragment : Fragment() {
//
//    private var _binding: FragmentAddItemBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: ItemViewModel by viewModels()
//
//    private val navigationArgs: TagItemToShopsFragmentArgs by navArgs()
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
//        // Retrieve the item name from Safe Args
////        val itemName = navigationArgs.itemName
//
////        val itemName = navigationArgs.itemName
//        val itemName =
//
//        binding.etItemName.setText(itemName)
//
//
//
//// Update the ViewModel with the item name
//
//
//        // Optionally, fetch item data from the ViewModel and bind it to the UI
//
//
//        binding.btnNext.setOnClickListener {
//            viewModel.updateItemName(itemName.toString())
//
//            viewModel.getItemNameLiveData().observe(viewLifecycleOwner) { item ->
//                Log.i("DOH!", "LiveData updated with item: $item")
//                item?.let {
//                    binding.etItemName.setText(it)
//                }
//            }
//
//            val action = AddItemFragmentDirections
//                .actionAddItemFragmentToTagItemToShopsFragment(itemName.toString())
//            Log.i("DOH!"," Item name in add item is $itemName")
//            findNavController().navigate(action)
//        }
//    }
//
////    private fun bind(item: String) {
////        binding.etItemName.setText(item, TextView.BufferType.SPANNABLE)
////    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


//@AndroidEntryPoint
//class AddItemFragment : Fragment() {
//
//    private var _binding: FragmentAddItemBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: ItemViewModel by viewModels()
//
//    private val navigationArgs: TagItemToShopsFragmentArgs by navArgs()
//
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
//    private fun bind(item: Item) {
//        binding.apply {
//            etItemName.setText(item.name, TextView.BufferType.SPANNABLE)
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // You can retrieve the item name from your ViewModel or any other source
//        val itemName = navigationArgs.itemName
//        binding.etItemName.setText(itemName)
//
//        if (!itemName.isNullOrEmpty()) {
//            // Set the item name in a TextView or EditText
//            viewModel.getItemName()
//        }
//
//        binding.btnNext.setOnClickListener {
//            val action = AddItemFragmentDirections
//                .actionAddItemFragmentToTagItemToShopsFragment(itemName)
//            findNavController().navigate(action)
//        }
//    }
//
//
//
//    private fun navigateToTagItemToShopsFragment(itemName: String?) {
//        // Check if the itemName is not null or empty before navigating
//
//        val itemName = "Example Item Name"
//
//        val action = AddItemFragmentDirections
//            .actionAddItemFragmentToTagItemToShopsFragment(itemName)
//        findNavController().navigate(action)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}





//@AndroidEntryPoint
//class AddItemFragment : Fragment() {
//
//
//    private var _binding: FragmentAddItemBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: ItemViewModel by viewModels()
//
//    private lateinit var itemName: ItemViewModel
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
////        val itemName = arguments?.getString("itemName")
//        if (!itemName.isNullOrEmpty()) {
//            // Set the item name in a TextView or EditText
//            binding.etItemName.setText(itemName)
//        }
//
//        binding.btnNext.setOnClickListener {
////            if (itemName != null) {
//                navigateToTagItemToShopsFragment()
////            }
//        }
//    }
//
//    private fun navigateToTagItemToShopsFragment() {
//        val action = AddItemFragmentDirections
//            .actionAddItemFragmentToTagItemToShopsFragment()
//        findNavController().navigate(action)
//    }
//
//
//
//
//}