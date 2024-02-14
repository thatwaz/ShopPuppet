package com.thatwaz.shoppuppet.util

import android.annotation.SuppressLint
import android.content.Context


class ResourceCache(private val context: Context) {
    private val colorResourceCache = mutableMapOf<String, Int>()
    private val drawableResourceCache = mutableMapOf<String, Int>()

    @SuppressLint("DiscouragedApi")
    fun getColorResId(colorResName: String): Int{
        return colorResourceCache.getOrPut(colorResName) {
            context.resources.getIdentifier(colorResName, "color", context.packageName)
        }
    }

    @SuppressLint("DiscouragedApi")
    fun getDrawableResId(drawableResName: String): Int {
        return drawableResourceCache.getOrPut(drawableResName) {
            context.resources.getIdentifier(drawableResName, "drawable", context.packageName)
        }
    }
}

