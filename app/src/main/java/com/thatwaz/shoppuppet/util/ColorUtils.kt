package com.thatwaz.shoppuppet.util

import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.FragmentAddShopBinding

object ColorUtils {


    fun getColorResName(viewId: Int): String? {
        return when (viewId) {
            R.id.shop_blue -> "shop_blue"
            R.id.shop_green -> "shop_green"
            R.id.shop_fashion_red -> "shop_fashion_red"
            R.id.shop_dark_gray -> "shop_dark_gray"
            R.id.shop_red -> "shop_red"
            R.id.shop_orange -> "shop_orange"
            R.id.shop_pink -> "shop_pink"
            R.id.shop_navy_blue -> "shop_navy_blue"
            R.id.shop_yellow -> "shop_yellow"
            R.id.shop_purple -> "shop_purple"
            R.id.shop_teal -> "shop_teal"
            R.id.shop_brown -> "shop_brown"
            else -> null
        }
    }


    fun updateIconColor(binding: FragmentAddShopBinding, colorResName: String, context: Context) {

        val colorResId = context.resources.getIdentifier(colorResName, "color", context.packageName)
        if (colorResId != 0) {
            val color = ContextCompat.getColor(context, colorResId)
            binding.previewIcon.setColorFilter(color)
            binding.initialsPreview.setTextColor(color)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                binding.cvShopPreview.outlineSpotShadowColor = color
            }

        } else {
            val defaultColor = ContextCompat.getColor(context, R.color.black)
            binding.previewIcon.setColorFilter(defaultColor)
            binding.initialsPreview.setTextColor(defaultColor)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                binding.cvShopPreview.outlineSpotShadowColor = defaultColor
            }
        }
    }

}
