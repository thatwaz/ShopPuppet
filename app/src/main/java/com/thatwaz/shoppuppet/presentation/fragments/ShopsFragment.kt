package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.FragmentShopsBinding
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.presentation.adapters.ShopAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopsViewModel
import com.thatwaz.shoppuppet.util.ResourceCache
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopsFragment : Fragment() {

    private var _binding: FragmentShopsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding cannot be accessed.")
    private val shopsViewModel: ShopsViewModel by viewModels()

    private lateinit var shopAdapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resourceCache = ResourceCache(requireContext())
        shopAdapter = ShopAdapter(resourceCache)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeShopData()
        setupAddShopButton()
        observeErrorMessages()
        setupEditDeleteInfoTextView()
        setupAdapterClickListeners()

        // Refreshes item count for each shop
        shopsViewModel.fetchShopsWithItemCount()
    }

    private fun observeErrorMessages() {
        shopsViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupEditDeleteInfoTextView() {
        val content = SpannableString(binding.tvEditDeleteInfo.text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        binding.tvEditDeleteInfo.text = content
        binding.tvEditDeleteInfo.setOnClickListener {
            showEditDeleteInstructionsDialog()
        }
    }

    private fun setupAdapterClickListeners() {
        shopAdapter.onShopItemClickListener = object : ShopAdapter.OnShopItemClickListener {
            override fun onShopItemClick(shop: Shop) {
                navigateToShopSpecificListFragment(shop)
            }
        }
        shopAdapter.onShopItemLongClickListener = object : ShopAdapter.OnShopItemLongClickListener {
            override fun onShopItemLongClick(shop: Shop) {
                showLongPressDialog(shop)
            }
        }
    }

    private fun navigateToShopSpecificListFragment(shop: Shop) {
        val action = ShopsFragmentDirections.actionShopsFragmentToShopSpecificListFragment(
            shop.name, shop.colorResName, shop.id
        )
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.shopsRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = shopAdapter
    }


    private fun observeShopData() {
        shopsViewModel.shopsWithItemCount.observe(viewLifecycleOwner) { shopListWithItemCount ->
            shopAdapter.submitList(shopListWithItemCount)
        }
    }


    private fun setupAddShopButton() {
        binding.btnAddShop.setOnClickListener {
            val action = ShopsFragmentDirections
                .actionShopsFragmentToAddShopFragment()
            findNavController().navigate(action)
        }
    }


    private fun showLongPressDialog(shop: Shop) {
        val context = requireContext()
        AlertDialog.Builder(context)
            .setTitle("Shop Actions")
            .setMessage("Choose an action for ${shop.name}")
            .setPositiveButton("Delete") { dialog, _ ->
                shopsViewModel.deleteShop(shop)
                dialog.dismiss()
                shopsViewModel.fetchShopsWithItemCount()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Edit") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showEditDeleteInstructionsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Edit/Delete Shop")
            .setMessage("Long press on a shop to edit or delete it.")
            .setPositiveButton("OK", null)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
