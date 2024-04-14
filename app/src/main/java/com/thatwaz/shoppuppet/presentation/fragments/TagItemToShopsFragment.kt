package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentTagItemToShopsBinding
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithSelection
import com.thatwaz.shoppuppet.presentation.adapters.ShopSelectionAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ItemViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.SelectedShopsViewModel
import com.thatwaz.shoppuppet.presentation.viewmodel.TagItemToShopsViewModel
import com.thatwaz.shoppuppet.util.ResourceCache
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TagItemToShopsFragment : BaseFragment() {

    private var _binding: FragmentTagItemToShopsBinding? = null
    private val binding: FragmentTagItemToShopsBinding
        get() = _binding ?: throw IllegalStateException("Binding cannot be accessed.")

    private val tagItemToShopsViewModel: TagItemToShopsViewModel by viewModels()
    private val selectedShopsViewModel: SelectedShopsViewModel by viewModels()
    private val itemViewModel: ItemViewModel by viewModels()

    private val resourceCache by lazy { ResourceCache(requireContext()) }

    private val shopSelectionAdapter: ShopSelectionAdapter by lazy {
        ShopSelectionAdapter(
            { shop, isChecked -> handleShopSelection(shop, isChecked) },
            resourceCache
        )
    }

    private var isPriority = false
    private var itemId: Long = -1

    private val navigationArgs: TagItemToShopsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagItemToShopsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializations
        initArguments()

        // UI Setup
        setupPriorityIcon()
        setupRecyclerView()
        setupSaveButton()

        // Observers
        observeShopData()
        observeAndDisplayShops()
        setupErrorObservers()

    }

    override fun showUserGuideDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.user_guide_title))
            .setMessage(Html.fromHtml(getString(R.string.tag_item_to_shops), Html.FROM_HTML_MODE_COMPACT))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun handleShopSelection(shop: Shop, isChecked: Boolean) {
        tagItemToShopsViewModel.toggleShopSelection(shop)
        if (isChecked) {
            selectedShopsViewModel.addSelectedShop(shop)
        } else {
            selectedShopsViewModel.removeSelectedShop(shop)
        }
    }

    private fun setupRecyclerView() {
        // Initializing the RecyclerView for displaying shop selections
        val recyclerView: RecyclerView = binding.rvShopsToTag
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = shopSelectionAdapter

    }


    private fun observeAndDisplayShops() {
        // Request the ViewModel to fetch shops associated with the current item and update the LiveData
        tagItemToShopsViewModel.fetchAndSetSelectedShops(itemId)

        // Observe changes in the selected shops LiveData. This LiveData is updated when
        // the list of shops associated with the current item changes, either due to an initial fetch
        // or subsequent updates in the selection.
        tagItemToShopsViewModel.selectedShopsLiveData.observe(viewLifecycleOwner) { shopsWithSelection ->

            // Update the RecyclerView adapter with the new list of shops. This will refresh
            // the UI to display the current selection state (selected/not selected) for each shop.
            shopSelectionAdapter.submitList(shopsWithSelection)
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            if (!validateSelectedShops()) {
                showShopSelectionError()
                return@setOnClickListener
            }

            handleSaveButtonClick()
        }
    }

    private fun validateSelectedShops(): Boolean {
        return selectedShopsViewModel.selectedShops.value?.isNotEmpty() ?: false
    }

    private fun showShopSelectionError() {
        Toast.makeText(context, "Please select at least one shop", Toast.LENGTH_SHORT).show()
    }

    private fun handleSaveButtonClick() {
        val itemName = binding.tvItemName.text.toString()
        val selectedShopIds =
            selectedShopsViewModel.selectedShops.value?.map { it.id } ?: emptyList()

        // Start the save operation
        itemViewModel.handleItemSave(itemId, itemName, selectedShopIds, isPriority)


        // Observe the save operation completion
        itemViewModel.saveOperationComplete.observe(viewLifecycleOwner) { isComplete ->
            viewLifecycleOwner.lifecycleScope.launch {
                delay(200)
                if (isComplete == true) { // Use == true to safely handle null
                    Toast.makeText(context, "Item Saved.", Toast.LENGTH_SHORT).show()
                    navigateToListFragment()
                    itemViewModel.resetSaveOperationStatus()
                } else if (isComplete == false) {
                    showSaveError()
                }
            }
        }
    }

    private fun showSaveError() {
        Toast.makeText(context, "Save Error Occurred.", Toast.LENGTH_SHORT).show()
    }


    private fun navigateToListFragment() {
        findNavController().navigate(
            TagItemToShopsFragmentDirections.actionTagItemToShopsFragmentToListFragment()
        )
    }

    private fun initArguments() {
        // Extracting item name, ID, and priority status from navigation args
        // These are used for initializing UI components and handling item-specific logic
        val itemName = arguments?.getString("itemName") ?: "Default Name"
        binding.tvItemName.setText(itemName)

        itemId = navigationArgs.itemId
        isPriority = navigationArgs.isPriority

        val associatedShopIds = navigationArgs.associatedShopIds.toList()
        selectedShopsViewModel.setSelectedShopIds(associatedShopIds)
    }


    private fun setupPriorityIcon() {
        // Setting up the priority icon click listener to toggle item's priority status
        updatePriorityIcon()
        binding.ivPriorityStar.setOnClickListener {
            // Toggle the priority status
            isPriority = !isPriority
            updatePriorityIcon() // Update UI based on new priority status
        }
    }


    private fun updatePriorityIcon() {
        // Update the UI to reflect the current priority status of the item
        if (isPriority) {
            binding.ivPriorityStar.setImageResource(R.drawable.ic_star)
            binding.ivPriorityStar.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            binding.tvItemName.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )

        } else {
            binding.ivPriorityStar.setImageResource(R.drawable.ic_star_outline)
            binding.ivPriorityStar.colorFilter = null
            binding.tvItemName.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.off_white
                )
            )
        }
    }


    private fun observeShopData() {
        tagItemToShopsViewModel.shops.observe(viewLifecycleOwner) { shops ->
            val selectedShopIds =
                selectedShopsViewModel.selectedShops.value?.map { it.id }?.toSet() ?: emptySet()

            val shopsWithSelection = shops.map { shop ->
                ShopWithSelection(
                    shop = shop,
                    isSelected = selectedShopIds.contains(shop.id)
                )
            }
            shopSelectionAdapter.submitList(shopsWithSelection)
        }
    }

    private fun setupErrorObservers() {
        // Observing error messages from ViewModels and displaying them as Toasts
        itemViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        selectedShopsViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        tagItemToShopsViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}







