@file:Suppress("BooleanMethodIsAlwaysInverted")

package com.thatwaz.shoppuppet.presentation.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentAddShopBinding
import com.thatwaz.shoppuppet.presentation.viewmodel.AddShopViewModel
import com.thatwaz.shoppuppet.util.ColorUtils
import com.thatwaz.shoppuppet.util.IconUtils
import com.thatwaz.shoppuppet.util.KeyboardUtils
import dagger.hilt.android.AndroidEntryPoint

@Suppress("BooleanMethodIsAlwaysInverted")
@AndroidEntryPoint
class AddShopFragment : BaseFragment(), CustomIconDialogFragment.CustomIconDialogListener {

    private val addShopViewModel: AddShopViewModel by viewModels()
    private var _binding: FragmentAddShopBinding? = null

    private val binding
        get() = _binding ?: throw IllegalStateException("Binding cannot be accessed.")

    private var selectedIconRef: String = "ic_shop"
    private var selectedColor: String = "inactive_grey"

    private var shopInitials: String? = null

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

        setupInitialButtonStates()
        initializeViews()
        setupTextWatcher()
        setupObservers()

    }

    override fun showUserGuideDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.user_guide_title))
            .setMessage(Html.fromHtml(getString(R.string.add_new_shop_user_guide_message), Html.FROM_HTML_MODE_COMPACT))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun setupInitialButtonStates() {
        // Set initial button states (enable/disable)
        disableButtons(binding.btnSaveShop, binding.btnShopIcon, binding.btnShopName)
    }

    private fun initializeViews() {
        binding.apply {
            cvChooseIcon.visibility = View.GONE
            ibAlphabet.setOnClickListener { showIconDialog() }
            btnShopIcon.setOnClickListener { toggleIconView() }
            btnShopName.setOnClickListener { handleShopNameInput() }
            btnSaveShop.setOnClickListener { handleShopSave() }
            btnStartOver.setOnClickListener { resetUI() }
        }
        setupIconClickListeners()
        setupColorClickListeners()
    }

    private fun setupTextWatcher() {
        // Sets up a TextWatcher to enable the shop name button when valid input is detected
        binding.etShopName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val shopName = s.toString()

                binding.btnShopName.apply {
                    isEnabled = isShopNameValid(shopName)
                    alpha = 1f
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
    }

    private fun setupObservers() {
        // Set up observers for ViewModel LiveData
        addShopViewModel.shopName.observe(viewLifecycleOwner) { name ->
            binding.shopNamePreview.text = name
        }
        observeSaveStatus()
        observeErrorMessages()
    }

    // Shows a custom icon dialog for selecting shop initials vs. icons
    private fun showIconDialog() {
        val dialog = CustomIconDialogFragment()
        dialog.show(childFragmentManager, "CustomIconDialog")
    }

    // Removes Choose Icon Card View and allows for Choose Color Card View to be visible
    private fun toggleIconView() {
        binding.apply {
            cvChooseColor.visibility = View.VISIBLE
            cvChooseIcon.visibility = View.GONE
            btnShopIcon.visibility = View.GONE
        }
    }


     //Handles click events on color views to update the selected color and the save button state
    private fun onColorClicked(view: View) {
        val colorResName = ColorUtils.getColorResName(view.id)
        colorResName?.let { name ->
            ColorUtils.updateIconColor(binding, name, requireContext())
            selectedColor = name
            updateSaveButtonState()
        }
    }

    private fun handleIconClick(view: View) {
        val iconResName = IconUtils.getIconResName(view.id)
        iconResName.let { resName ->
            // Update the preview icon based on the resource name
            updatePreviewIconByName(resName)
            // Save the icon name
            selectedIconRef = resName
        }
    }



    @SuppressLint("ResourceType")
    private fun updatePreviewIconByName(name: String) {
        // Check if the name matches the pattern of an icon name
        if (name.startsWith("ic_")) {
            val iconResId = resources.getIdentifier(name, "drawable", context?.packageName ?: "")
            if (iconResId != 0) {
                binding.previewIcon.setImageResource(iconResId)
                binding.initialsPreview.text = "" // Clear initials if an icon is set
                updateIconButtonState()
            }
        } else {
            binding.previewIcon.setImageResource(0) // Clear icon
            binding.initialsPreview.text = name // Display initials
        }
    }

    private fun updateSaveButtonState() {
        // Enable the save button if a color is selected
        binding.btnSaveShop.apply {
            isEnabled =
                selectedColor.isNotBlank() && isShopNameValid(binding.etShopName.text.toString())
            alpha = 1f
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

    private fun observeSaveStatus() {
        addShopViewModel.saveStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, "Shop saved!", Toast.LENGTH_SHORT).show()
                navigateToShopsFragment()
            }
            result.onFailure {
                Toast.makeText(
                    context,
                    it.message ?: "Failed to save shop. Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleShopSave() {
        val shopName = binding.etShopName.text.toString()
        updateShopDetails(shopName)
        addShopViewModel.saveShop()
    }

    private fun isShopNameValid(shopName: String): Boolean {
        return shopName.isNotBlank()
    }


    private fun updateShopDetails(shopName: String) {
        addShopViewModel.updateShopName(shopName)
        addShopViewModel.updateSelectedIconRef(selectedIconRef)
        addShopViewModel.updateSelectedColor(selectedColor)
        addShopViewModel.updateShopInitials(shopInitials)
    }


    private fun navigateToShopsFragment() {
        val action = AddShopFragmentDirections.actionAddShopFragmentToShopsFragment()
        findNavController().navigate(action)
    }

    private fun setupIconClickListeners() {
        val icons = listOf(
            binding.ibGroceryStore, binding.ibPharmacy, binding.ibHardware, binding.ibStorefront,
            binding.ibTelevision, binding.ibPets, binding.ibStore,
            binding.ibStroller, binding.ibBooks, binding.ibBullseye, binding.ibCar, binding.ibBank, binding.ibHome
        )
        icons.forEach { iconView ->
            iconView.setOnClickListener {
                handleIconClick(it)
                binding.btnShopIcon.alpha = 1f
            }
        }
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

    override fun onIconTextEntered(text: String) {
        // Assume no icon is selected, so clear any icon selection
        binding.previewIcon.setImageResource(0)
        binding.initialsPreview.text = text
        shopInitials = text
        selectedIconRef = text // Save initials as the selected icon reference
        updateIconButtonState()
        binding.btnShopIcon.alpha = 1f
    }

    private fun updateIconButtonState() {
        // Enable the save button if either an icon is selected or initials are entered
        binding.btnShopIcon.isEnabled = true
    }


    private fun resetUI() {
        binding.etShopName.alpha = 1f
        binding.btnShopName.alpha = 1f
        binding.etShopName.isEnabled = true
        binding.cvChooseIcon.visibility = View.INVISIBLE
        binding.cvChooseColor.visibility = View.INVISIBLE
        binding.etShopName.text?.clear()
        binding.shopNamePreview.text = ""
        selectedColor = "inactive_grey"
        ColorUtils.updateIconColor(binding, selectedColor, requireContext())
        selectedIconRef = null.toString()

        shopInitials = null

        // Reset icon and initials preview
        binding.previewIcon.setImageResource(0)  // Clear the icon image
        binding.initialsPreview.text = ""

        // Reset button states
        disableButtons(binding.btnSaveShop, binding.btnShopIcon, binding.btnShopName)

    }

    // Observes error messages from ViewModel and displays them as Toast messages
    private fun observeErrorMessages() {
        addShopViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun disableButtons(vararg buttons: View) {
        buttons.forEach { button ->
            button.isEnabled = false
            button.alpha = 0.5f
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

