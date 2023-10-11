package com.thatwaz.shoppuppet.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thatwaz.shoppuppet.databinding.FragmentAddShopBinding


class AddShopFragment : Fragment() {
    // Declare your binding variable
    private var _binding: FragmentAddShopBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding object
        _binding = FragmentAddShopBinding.inflate(inflater, container, false)

        // Initially set your CardView as gone
        binding.cvChooseIcon.visibility = View.GONE

        binding.ibAlphabet.setOnClickListener {
            val dialog = CustomIconDialogFragment()
            dialog.show(childFragmentManager, "CustomIconDialog")
        }


        // Set the click listener for the next button
        binding.btnShopName.setOnClickListener {
            // Disable the EditText
            binding.etShopName.isEnabled = false
            binding.btnShopIcon.visibility = View.VISIBLE

            // Dim the EditText
            binding.etShopName.alpha = 0.5f
            binding.btnShopName.alpha = 0.5f
            // You might want to add some validation logic for the EditText here

            // Make the CardView visible
            binding.cvChooseIcon.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leak
        _binding = null
    }
}
