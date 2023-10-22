package com.thatwaz.shoppuppet.util

import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentAddShopBinding

object ColorUtils {


    fun getColorResId(viewId: Int): Int? {
        return when (viewId) {
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
            else -> null
        }
    }

    fun updateIconColor(binding: FragmentAddShopBinding, colorResId: Int, context: Context) {
        val color = ContextCompat.getColor(context, colorResId)
        binding.previewIcon.setColorFilter(color)
        binding.initialsPreview.setTextColor(color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.cvShopPreview.outlineSpotShadowColor = color
        } else {
            // You can provide an alternative way to handle shadow or just skip it.
        }
    }
}
