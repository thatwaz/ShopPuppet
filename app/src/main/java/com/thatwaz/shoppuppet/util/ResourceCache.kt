package com.thatwaz.shoppuppet.util

import android.content.Context

class ResourceCache(private val context: Context) {
    private val resourceCache = mutableMapOf<String, Int>()

    fun getColorResId(resourceName: String, defType: String = "color"): Int {
        return resourceCache.getOrPut(resourceName) {
            context.resources.getIdentifier(resourceName, defType, context.packageName)
        }
    }
}
