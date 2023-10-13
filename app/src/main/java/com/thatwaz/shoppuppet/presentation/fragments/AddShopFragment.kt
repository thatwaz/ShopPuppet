package com.thatwaz.shoppuppet.presentation.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.thatwaz.shoppuppet.R
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




        binding.btnShopIcon.setOnClickListener {
            binding.cvChooseColor.visibility = View.VISIBLE
            binding.cvChooseIcon.visibility = View.GONE
            binding.btnShopIcon.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnShopName.setOnClickListener {
            val enteredText = binding.etShopName.text.toString()
            binding.shopNamePreview.text = enteredText
            binding.etShopName.isEnabled = false
            binding.btnShopIcon.visibility = View.VISIBLE

            // Dim the EditText
            binding.etShopName.alpha = 0.5f
            binding.btnShopName.alpha = 0.5f
            // You might want to add some validation logic for the EditText here

            // Make the CardView visible
            binding.cvChooseIcon.visibility = View.VISIBLE

            // Optionally hide the keyboard after button press
            hideKeyboard()

            val iconClickListener = View.OnClickListener { view ->
                when (view.id) {
                    R.id.ib_grocery_store -> updatePreviewIcon(R.drawable.ic_grocery_store)
                    R.id.ib_pharmacy -> updatePreviewIcon(R.drawable.ic_pharmacy)
                    R.id.ib_hardware -> updatePreviewIcon(R.drawable.ic_hardware)
                    R.id.ib_storefront -> updatePreviewIcon(R.drawable.ic_storefront)
                    R.id.ib_television -> updatePreviewIcon(R.drawable.ic_television)
                    R.id.ib_shopping_bag -> updatePreviewIcon(R.drawable.ic_shopping_bag)
                    R.id.ib_store -> updatePreviewIcon(R.drawable.ic_store)
                    R.id.ib_stroller -> updatePreviewIcon(R.drawable.ic_stroller)
                    R.id.ib_books -> updatePreviewIcon(R.drawable.ic_books)
                    R.id.ib_bullseye -> updatePreviewIcon(R.drawable.ic_bullseye)

                    // ... handle other icons
                }
            }

            binding.apply {
                ibGroceryStore.setOnClickListener(iconClickListener)
                ibPharmacy.setOnClickListener(iconClickListener)
                ibHardware.setOnClickListener(iconClickListener)
                ibStorefront.setOnClickListener(iconClickListener)
                ibTelevision.setOnClickListener(iconClickListener)
                ibShoppingBag.setOnClickListener(iconClickListener)
                ibStore.setOnClickListener(iconClickListener)
                ibStroller.setOnClickListener(iconClickListener)
                ibBooks.setOnClickListener(iconClickListener)
                ibBullseye.setOnClickListener(iconClickListener)
            }
            @RequiresApi(Build.VERSION_CODES.P)
            fun onColorClicked(view: View) {
                val selectedColor = when(view.id) {
                    R.id.shop_blue -> R.color.shop_blue
                    R.id.shop_green -> R.color.shop_green
                    R.id.shop_fashion_red -> R.color.shop_fashion_red
                    R.id.shop_dark_gray -> R.color.shop_dark_gray
                    R.id.shop_red -> R.color.shop_red
                    R.id.shop_orange -> R.color.shop_orange
                    R.id.shop_pink -> R.color.shop_pink
                    R.id.shop_navy_blue -> R.color.shop_navy_blue
                    R.id.shop_yellow -> R.color.shop_yellow
                    R.id.shop_purple -> R.color.shop_purple
                    R.id.shop_teal -> R.color.shop_teal
                    R.id.shop_brown -> R.color.shop_brown
                    // ... other color mappings ...
                    else -> return
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    updateIconColor(selectedColor)
                }
            }

            binding.apply {
                shopBlue.setOnClickListener(::onColorClicked)
                shopGreen.setOnClickListener(::onColorClicked)
                shopFashionRed.setOnClickListener(::onColorClicked)
                shopDarkGray.setOnClickListener(::onColorClicked)
                shopRed.setOnClickListener(::onColorClicked)
                shopOrange.setOnClickListener(::onColorClicked)
                shopPink.setOnClickListener(::onColorClicked)
                shopNavyBlue.setOnClickListener(::onColorClicked)
                shopYellow.setOnClickListener(::onColorClicked)
                shopPurple.setOnClickListener(::onColorClicked)
                shopPink.setOnClickListener(::onColorClicked)
                shopTeal.setOnClickListener(::onColorClicked)
                shopBrown.setOnClickListener(::onColorClicked)

            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun updateIconColor(colorResId: Int) {
        val color = ContextCompat.getColor(requireContext(), colorResId)
        binding.previewIcon.setColorFilter(color)
        binding.cvShopPreview.outlineSpotShadowColor = color
    }



    private fun updatePreviewIcon(drawableResId: Int,) {
        binding.previewIcon.setImageResource(drawableResId)


    }





    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leak
        _binding = null
    }

}
