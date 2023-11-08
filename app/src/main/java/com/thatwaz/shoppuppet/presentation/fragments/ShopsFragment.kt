package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.FragmentShopsBinding
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.presentation.adapters.ShopAdapter
import com.thatwaz.shoppuppet.presentation.viewmodel.ShopsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopsFragment : Fragment() {

    private var _binding: FragmentShopsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShopsViewModel by viewModels()
    private lateinit var shopAdapter: ShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeShopData()
        setupAddShopButton()


        shopAdapter.onShopItemClickListener = object : ShopAdapter.OnShopItemClickListener {
            override fun onShopItemClick(shop: Shop) {
                // Here you define your navigation logic
//                val shopId: Long = 1// get the shop ID from the selected shop
                val action = ShopsFragmentDirections.actionShopsFragmentToStoreSpecificListFragment(shop.id)
                findNavController().navigate(action)
//
//                val action = ShopsFragmentDirections.actionShopsFragmentToStoreSpecificListFragment()
//                findNavController().navigate(action)
            }
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.shopsRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        shopAdapter = ShopAdapter()
        recyclerView.adapter = shopAdapter
    }

    private fun observeShopData() {
        viewModel.allShops.observe(viewLifecycleOwner) { shopList ->
            shopAdapter.submitList(shopList)
        }
    }

    private fun setupAddShopButton() {
        binding.btnAddShop.setOnClickListener {
            val action = ShopsFragmentDirections
                .actionShopsFragmentToAddShopFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}








//class ShopsFragment : Fragment() {
//
//
//
//    private var _binding: FragmentShopsBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var viewModel: ShopViewModel
//    private lateinit var shopAdapter: ShopAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        _binding = FragmentShopsBinding.inflate(inflater,container,false)
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize your RecyclerView and its adapter
//        val recyclerView: RecyclerView = view.findViewById(R.id.shopsRecyclerView)
//        recyclerView.layoutManager = GridLayoutManager(context,3)
//
//
//        // Create mock data or retrieve it from a ViewModel
//        val shops = MockDataStore.getShops()
//        shopAdapter = ShopAdapter(shops)
//        recyclerView.adapter = shopAdapter
//
//
//
//        binding.btnAddShop.setOnClickListener {
//            val action = ShopsFragmentDirections
//                .actionShopsFragmentToAddShopFragment()
//            findNavController().navigate(action)
//        }
//
//    }
//}
