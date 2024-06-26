package com.thatwaz.shoppuppet.util

import com.thatwaz.shoppuppet.R

object IconUtils {
    fun getIconResName(viewId: Int): String {
        return when (viewId) {
            R.id.ib_grocery_store -> "ic_grocery_store"
            R.id.ib_pharmacy -> "ic_pharmacy"
            R.id.ib_hardware -> "ic_hardware"
            R.id.ib_storefront -> "ic_storefront"
            R.id.ib_television -> "ic_television"
            R.id.ib_pets -> "ic_pets"
            R.id.ib_store -> "ic_store"
            R.id.ib_stroller -> "ic_stroller"
            R.id.ib_books -> "ic_books"
            R.id.ib_bullseye -> "ic_bullseye_icon"
            R.id.ib_car -> "ic_car"
            R.id.ib_bank -> "ic_bank2"
            R.id.ib_home -> "ic_home"
            else -> "ic_store"
        }
    }

}
