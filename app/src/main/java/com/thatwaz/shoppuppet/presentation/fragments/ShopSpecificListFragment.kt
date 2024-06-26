package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentShopSpecificListBinding
import com.thatwaz.shoppuppet.presentation.adapters.PurchasedItemsAdapter
import com.thatwaz.shoppuppet.presentation.adapters.ShopSpecificItemAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopSpecificListViewModel
import com.thatwaz.shoppuppet.util.ResourceCache
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShopSpecificListFragment : BaseFragment() {

    private val shopSpecificListViewModel: ShopSpecificListViewModel by viewModels()

    private var shopSpecificItemAdapter: ShopSpecificItemAdapter? = null
    private var purchasedItemsAdapter: PurchasedItemsAdapter? = null

    private val navigationArgs: ShopSpecificListFragmentArgs by navArgs()

    private lateinit var resourceCache: ResourceCache

    private var _binding: FragmentShopSpecificListBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding cannot be accessed.")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the resource cache to efficiently manage color resource lookups
        resourceCache = ResourceCache(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopSpecificListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeUI()
        setupEventListeners()
        fetchData()
        observeViewModel()

    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.shop_specific_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_user_guide -> {
                    showUserGuideDialog()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun showUserGuideDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.user_guide_title))
            .setMessage(Html.fromHtml(getString(R.string.shop_user_guide), Html.FROM_HTML_MODE_COMPACT))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun initializeUI() {
        val shopColorResName = navigationArgs.shopColorResId
        val colorResId = resourceCache.getColorResId(shopColorResName)
        val color = if (colorResId != 0) ContextCompat.getColor(
            requireContext(),
            colorResId
        ) else Color.BLACK

        binding.tvShopName.text = navigationArgs.shopName
        binding.clShopName.setBackgroundColor(color)
        binding.btnDeletePurchasedItems.backgroundTintList = ColorStateList.valueOf(color)
        setupAdapters(colorResId)
        setupRecyclerViews()
    }

    private fun setupEventListeners() {
        binding.btnDeletePurchasedItems.setOnClickListener {
            //Items get soft-deleted when marked as purchased button is clicked
            handleDeletePurchasedItems()
        }

        binding.btnBackToShops.setOnClickListener {
            navigateBackToShops()
        }
        binding.btnShopSpecificMenu.setOnClickListener { v ->
            showPopupMenu(v)
        }

    }

    private fun fetchData() {
        val shopId = navigationArgs.shopId
        shopSpecificListViewModel.setShopId(shopId)
        shopSpecificListViewModel.fetchShopSpecificItems(shopId)
    }

    private fun observeViewModel() {
        // Observe changes from the ViewModel to update UI components accordingly
        shopSpecificListViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        shopSpecificListViewModel.itemsToAcquire.observe(viewLifecycleOwner) { items ->
            shopSpecificItemAdapter?.submitList(items)
        }

        shopSpecificListViewModel.purchasedAndNotSoftDeletedItems
            .observe(viewLifecycleOwner) { items ->

            purchasedItemsAdapter?.submitList(items)
        }

    }

    private fun handleDeletePurchasedItems() {
        val checkedItems = purchasedItemsAdapter?.getCheckedItems() ?: listOf()
        shopSpecificListViewModel.softDeleteCheckedItems(checkedItems)
    }

    private fun navigateBackToShops() {
        findNavController()
            .navigate(
                ShopSpecificListFragmentDirections
                    .actionShopSpecificListFragmentToShopsFragment()
            )
    }


    private fun createCheckboxColorStateList(shopColorResId: Int): ColorStateList {
        // Create a ColorStateList for checkbox coloring based on the shop's theme color
        val shopColor = if (shopColorResId != 0) ContextCompat.getColor(
            requireContext(),
            shopColorResId
        ) else Color.LTGRAY // Fallback to gray if not found
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

    private fun setupAdapters(shopColor: Int) {
        val colorStateList = createCheckboxColorStateList(shopColor)
        shopSpecificItemAdapter = ShopSpecificItemAdapter(colorStateList) { item ->
            if (!item.isPurchased) {
                shopSpecificListViewModel.handleUnpurchasedItemChecked(item)

            }
        }

        purchasedItemsAdapter = PurchasedItemsAdapter(colorStateList) { item ->
            if (item.isPurchased) {
                shopSpecificListViewModel.handlePurchasedItemChecked(item)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        shopSpecificItemAdapter = null
        purchasedItemsAdapter = null
    }

}





