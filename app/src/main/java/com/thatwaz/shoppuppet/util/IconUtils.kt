package com.thatwaz.shoppuppet.util

import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentAddShopBinding

object IconUtils {


//    fun getIconResId(viewId: Int): Int? {
//        return when (viewId) {
//            R.id.ib_grocery_store -> R.drawable.ic_grocery_store
//            R.id.ib_pharmacy -> R.drawable.ic_pharmacy
//            R.id.ib_hardware -> R.drawable.ic_hardware
//            R.id.ib_storefront -> R.drawable.ic_storefront
//            R.id.ib_television -> R.drawable.ic_television
//            R.id.ib_shopping_bag -> R.drawable.ic_shopping_bag
//            R.id.ib_store -> R.drawable.ic_store
//            R.id.ib_stroller -> R.drawable.ic_stroller
//            R.id.ib_books -> R.drawable.ic_books
//            R.id.ib_bullseye -> R.drawable.ic_bullseye
//            else -> null
//        }
//    }



    fun getIconResName(viewId: Int): String? {
        return when (viewId) {
            R.id.ib_grocery_store -> "ic_grocery_store"
            R.id.ib_pharmacy -> "ic_pharmacy"
            R.id.ib_hardware -> "ic_hardware"
            R.id.ib_storefront -> "ic_storefront"
            R.id.ib_television -> "ic_television"
            R.id.ib_shopping_bag -> "ic_shopping_bag"
            R.id.ib_store -> "ic_store"
            R.id.ib_stroller -> "ic_stroller"
            R.id.ib_books -> "ic_books"
            R.id.ib_bullseye -> "ic_bullseye"
            else -> "ic_store"
        }
    }

//
//        fun getIconResName(viewId: Int): String? {
//            val iconName = when (viewId) {
//
//                R.id.ib_grocery_store -> "ic_grocery_store"
//                R.id.ib_pharmacy -> "ic_pharmacy"
//                R.id.ib_hardware -> "ic_hardware"
//                R.id.ib_storefront -> "ic_storefront"
//                R.id.ib_television -> "ic_television"
//                R.id.ib_shopping_bag -> "ic_shopping_bag"
//                R.id.ib_store -> "ic_store"
//                R.id.ib_stroller -> "ic_stroller"
//                R.id.ib_books -> "ic_books"
//                R.id.ib_bullseye -> "ic_bullseye"
//                else -> "ic_store"
//            }
//            Log.d("getIconResName", "Icon Name: $iconName for viewId: $viewId")
//            return iconName
//        }

        // Additional utility methods as needed...






    fun updatePreviewIcon(binding: FragmentAddShopBinding, drawableResId: Int) {
        binding.previewIcon.setImageResource(drawableResId)
    }
}
