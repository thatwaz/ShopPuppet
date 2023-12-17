package com.thatwaz.shoppuppet.presentation.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.thatwaz.shoppuppet.databinding.FragmentShopSpecificListBinding
import com.thatwaz.shoppuppet.presentation.adapters.PurchasedItemsAdapter
import com.thatwaz.shoppuppet.presentation.adapters.ShopSpecificItemAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopSpecificListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShopSpecificListFragment : Fragment() {

    private val viewModel: ShopSpecificListViewModel by viewModels()

    private var shopSpecificItemAdapter: ShopSpecificItemAdapter? = null
    private var purchasedItemsAdapter: PurchasedItemsAdapter? = null

    private val navigationArgs: ShopSpecificListFragmentArgs by navArgs()

 //   val colorResId = requireContext().resources.getIdentifier(shopColorResName, "color", requireContext().packageName)


//    val colorResId = requireContext().resources.getIdentifier(shopColorResName, "color", requireContext().packageName)


    private var _binding: FragmentShopSpecificListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopSpecificListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FragmentLifecycle", "onViewCreated called for ShopSpecificListFragment")

        val shopId = navigationArgs.shopId
        val shopName = navigationArgs.shopName
        val shopColorResName = navigationArgs.shopColorResId
//        val colorResId = requireContext().resources.getIdentifier(shopColorResName, "color", requireContext().packageName)


        // Convert the color resource name to an actual color resource ID
        val colorResId = requireContext().resources.getIdentifier(shopColorResName, "color", requireContext().packageName)
        val color = if (colorResId != 0) ContextCompat.getColor(requireContext(), colorResId) else Color.BLACK // Fallback to a default color if not found
        Log.i("POOP","Shop color name is $colorResId")
        binding.tvShopName.text = shopName
        binding.clShopName.setBackgroundColor(color)

        // Set the background color of the FloatingActionButton
        binding.fabDeletePurchasedItems.backgroundTintList = ColorStateList.valueOf(color)
        Log.d("FragmentLifecycle", "Fetching items for shop ID: $shopId")

        viewModel.fetchShopSpecificItems(shopId) // This fetches data on view creation
        observeLiveData()
        setupAdapters(colorResId) // Pass color instead of shopColorResId
        setupRecyclerViews()

        binding.fabDeletePurchasedItems.setOnClickListener {
            val checkedItems = purchasedItemsAdapter?.getCheckedItems() ?: listOf()
            // Pass checkedItems to ViewModel for deletion
            viewModel.deleteCheckedItems(checkedItems)

        }

        binding.btnBackToShops.setOnClickListener {
            val action = ShopSpecificListFragmentDirections
                .actionShopSpecificListFragmentToShopsFragment()
            findNavController().navigate(action)

        }


        Log.d("FragmentLifecycle", "Adapters and RecyclerViews set up, LiveData observed")


    }


    private fun createCheckboxColorStateList(shopColorResId: Int): ColorStateList {
        val shopColor = if (shopColorResId != 0) ContextCompat.getColor(requireContext(), shopColorResId) else Color.LTGRAY // Fallback to gray if not found
        return ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked), // unchecked
                intArrayOf(android.R.attr.state_checked) // checked
            ),
            intArrayOf(
                Color.LTGRAY, // Color for unchecked
                shopColor // Color for checked
            )
        )
    }

//    private fun createCheckboxColorStateList(shopColor: Int): ColorStateList {
//        return ColorStateList(
//            arrayOf(
//                intArrayOf(-android.R.attr.state_checked), // unchecked
//                intArrayOf(android.R.attr.state_checked) // checked
//            ),
//            intArrayOf(
//                Color.LTGRAY, // Color for unchecked
//                ContextCompat.getColor(requireContext(), shopColor) // Color for checked
//            )
//        )
//    }


    private fun setupAdapters(shopColor: Int) {
        val colorStateList = createCheckboxColorStateList(shopColor)
        shopSpecificItemAdapter = ShopSpecificItemAdapter(colorStateList) { item ->
            if (!item.isPurchased) {
                viewModel.handleUnpurchasedItemChecked(item)
                Log.d("AdapterLog", "Unpurchased item checked: ${item}")
            }
        }

        purchasedItemsAdapter = PurchasedItemsAdapter(colorStateList) { item ->
            if (item.isPurchased) {
                viewModel.handlePurchasedItemChecked(item)
                Log.d("AdapterLog", "Purchased item unchecked: ${item.name}")
            }
        }
    }

    private fun setupRecyclerViews() {
        binding.rvUnpurchasedItems.apply {
            adapter = shopSpecificItemAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.rvPurchasedItems.apply {
            adapter = purchasedItemsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


    private fun observeLiveData() {
        viewModel.unpurchasedItems.observe(viewLifecycleOwner) { items ->
            shopSpecificItemAdapter?.submitList(items)
//            Log.i("DOH!","this things are: ${viewModel.purchasedItems.value}")
            Log.d(
                "FragmentLog",
                "Unpurchased items updated post-rotation: ${items.map { it.name }}"
            )
        }

        viewModel.purchasedItems.observe(viewLifecycleOwner) { items ->
            purchasedItemsAdapter?.submitList(items)
            Log.i("DOH!", "this things are: ${viewModel.purchasedItems.value}")
            Log.d("FragmentLog", "Purchased items updated post-rotation: ${items.map { it.name }}")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        shopSpecificItemAdapter = null
        purchasedItemsAdapter = null
        Log.d("FragmentLifecycle", "onDestroyView called for ShopSpecificListFragment")
    }


}





