package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentShopsBinding
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.presentation.adapters.ShopAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopsViewModel
import com.thatwaz.shoppuppet.util.ResourceCache
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopsFragment : BaseFragment() {

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
        setupAdapterClickListeners()
        setupNoShopsMessage()
        observeShops()


        // Refreshes item count for each shop
        shopsViewModel.fetchShopsWithItemCount()
    }

    private fun setupNoShopsMessage() {
        val noShopsText = Html.fromHtml(getString(R.string.you_currently_have_no_shops), Html.FROM_HTML_MODE_COMPACT)
        binding.tvNoShops.text = noShopsText
    }

    private fun observeShops() {
        shopsViewModel.shopsWithItemCount.observe(viewLifecycleOwner) { shops ->
            binding.tvNoShops.visibility = if (shops.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun showUserGuideDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.user_guide_title))
            .setMessage(Html.fromHtml(getString(R.string.my_shops_user_guide_message), Html.FROM_HTML_MODE_COMPACT))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()

    }

    private fun observeErrorMessages() {
        shopsViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
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
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle("Delete Shop")
            .setMessage("Deleting '${shop.name}' will require reassigning exclusive items to other shops.")
            .setPositiveButton("Delete") { dialog, _ ->
                shopsViewModel.deleteShop(shop)
                dialog.dismiss()
                shopsViewModel.fetchShopsWithItemCount()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
