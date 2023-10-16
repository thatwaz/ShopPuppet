package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentShopsBinding
import com.thatwaz.shoppuppet.presentation.adapters.ShopAdapter
import com.thatwaz.shoppuppet.util.mock.MockDataStore


class ShopsFragment : Fragment() {



    private var _binding: FragmentShopsBinding? = null
    private val binding get() = _binding!!
    private lateinit var shopAdapter: ShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShopsBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize your RecyclerView and its adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.shopsRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context,3)


        // Create mock data or retrieve it from a ViewModel
        val shops = MockDataStore.getShops()
        shopAdapter = ShopAdapter(shops)
        recyclerView.adapter = shopAdapter



        binding.btnAddShop.setOnClickListener {
            val action = ShopsFragmentDirections
                .actionShopsFragmentToAddShopFragment()
            findNavController().navigate(action)
        }

    }

//    override fun onResume() {
//        super.onResume()
//
//        val shops = MockDataStore.getShops()
//        // Assuming you've already initialized your ShopAdapter
//        ShopAdapter.updateShops(shops)
//    }





}
