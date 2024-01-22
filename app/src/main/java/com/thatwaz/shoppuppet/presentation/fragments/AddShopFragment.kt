package com.thatwaz.shoppuppet.presentation.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thatwaz.shoppuppet.databinding.FragmentAddShopBinding
import com.thatwaz.shoppuppet.presentation.viewmodel.AddShopViewModel
import com.thatwaz.shoppuppet.util.ColorUtils
import com.thatwaz.shoppuppet.util.IconUtils
import com.thatwaz.shoppuppet.util.KeyboardUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddShopFragment : Fragment(), CustomIconDialogFragment.CustomIconDialogListener {

    private val addShopViewModel: AddShopViewModel by viewModels()
    private var _binding: FragmentAddShopBinding? = null
    private val binding get() = _binding!!

    private var selectedIconRef: Int? = null
    private var selectedColor: String = "black" // Replace with your default color name

    //    private var selectedColor: Int = R.color.black
    private var shopInitials: String? = null

    @SuppressLint("ResourceType")
    private val iconClickListener = View.OnClickListener { view ->
        val iconResName = IconUtils.getIconResName(view.id)
        iconResName?.let { resName ->
            // Convert the resource name to an actual drawable resource ID
            val iconResId = resources.getIdentifier(resName, "drawable", context?.packageName ?: "")
            if (iconResId != 0) { // Check if resource ID is valid
                updatePreviewIcon(iconResId)
                selectedIconRef = iconResId
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        addShopViewModel.shopName.observe(viewLifecycleOwner) { name ->
            binding.shopNamePreview.text = name
        }

    }

    private fun initializeViews() {
        binding.apply {
            cvChooseIcon.visibility = View.GONE
            ibAlphabet.setOnClickListener { showIconDialog() }
            btnShopIcon.setOnClickListener { toggleIconView() }
            btnShopName.setOnClickListener { handleShopNameInput() }
            btnSaveShop.setOnClickListener { handleShopSave() }
        }
        setupIconClickListeners()
        setupColorClickListeners()
    }

    private fun showIconDialog() {
        val dialog = CustomIconDialogFragment()
        dialog.show(childFragmentManager, "CustomIconDialog")
    }

    private fun toggleIconView() {
        binding.apply {
            cvChooseColor.visibility = View.VISIBLE
            cvChooseIcon.visibility = View.GONE
            btnShopIcon.visibility = View.GONE
        }
    }


    private fun onColorClicked(view: View) {
        val colorResName = ColorUtils.getColorResName(view.id)
        colorResName?.let { name ->
            ColorUtils.updateIconColor(binding, name, requireContext())
            selectedColor = name
        }
    }

    private fun handleShopNameInput() {
        val enteredText = binding.etShopName.text.toString()
        binding.apply {
            shopNamePreview.text = enteredText
            etShopName.isEnabled = false
            btnShopIcon.visibility = View.VISIBLE
            etShopName.alpha = 0.5f
            btnShopName.alpha = 0.5f
            cvChooseIcon.visibility = View.VISIBLE
        }
        KeyboardUtils.hideKeyboard(requireView())
    }

    private fun handleShopSave() {
        val shopName = binding.etShopName.text.toString()
        if (shopName.isNotBlank()) {
            addShopViewModel.updateShopName(shopName)
            // Check if selectedIconRef is not null before updating
            addShopViewModel.updateSelectedIconRef((selectedIconRef ?: 0).toString()) // Use 0 or your 'no icon' indicator
            addShopViewModel.updateSelectedColor(selectedColor)
            addShopViewModel.updateShopInitials(shopInitials)

            if (addShopViewModel.saveShop()) {
                Toast.makeText(context, "Shop saved!", Toast.LENGTH_SHORT).show()
                navigateToShopsFragment()
            } else {
                Toast.makeText(context, "Failed to save shop. Try again.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please enter a valid shop name.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun navigateToShopsFragment() {
        val action = AddShopFragmentDirections.actionAddShopFragmentToShopsFragment()
        findNavController().navigate(action)
    }

    private fun setupIconClickListeners() {
        val icons = listOf(
            binding.ibGroceryStore, binding.ibPharmacy, binding.ibHardware, binding.ibStorefront,
            binding.ibTelevision, binding.ibShoppingBag, binding.ibStore,
            binding.ibStroller, binding.ibBooks, binding.ibBullseye
        )
        icons.forEach { it.setOnClickListener(iconClickListener) }
    }


    private fun setupColorClickListeners() {
        val colors = listOf(
            binding.shopBlue, binding.shopGreen, binding.shopFashionRed, binding.shopDarkGray,
            binding.shopRed, binding.shopOrange, binding.shopPink, binding.shopNavyBlue,
            binding.shopYellow, binding.shopPurple, binding.shopTeal, binding.shopBrown
        )

        colors.forEach { colorView ->
            colorView.setOnClickListener {
                onColorClicked(it)
            }
        }
    }


//    private fun setupColorClickListeners() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            val colors = listOf(
//                binding.shopBlue, binding.shopGreen, binding.shopFashionRed, binding.shopDarkGray,
//                binding.shopRed, binding.shopOrange, binding.shopPink, binding.shopNavyBlue,
//                binding.shopYellow, binding.shopPurple, binding.shopTeal, binding.shopBrown
//            )
//            colors.forEach { it.setOnClickListener(::onColorClicked) }
//        } else {
//            // Handle UI setup for devices below API 28 or hide certain UI elements.
//        }
//    }

    private fun updatePreviewIcon(drawableResId: Int) {
        binding.previewIcon.setImageResource(drawableResId)
    }

    override fun onIconTextEntered(text: String) {
        binding.initialsPreview.text = text
        shopInitials = text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

