package com.thatwaz.shoppuppet.util

import android.content.Context


class ResourceCache(private val context: Context) {
    private val colorResourceCache = mutableMapOf<String, Int>()
    private val drawableResourceCache = mutableMapOf<String, Int>()

    fun getColorResId(colorResName: String, defType: String = "color"): Int{
        return colorResourceCache.getOrPut(colorResName) {
            context.resources.getIdentifier(colorResName, "color", context.packageName)
        }
    }

    fun getDrawableResId(drawableResName: String): Int {
        return drawableResourceCache.getOrPut(drawableResName) {
            context.resources.getIdentifier(drawableResName, "drawable", context.packageName)
        }
    }
}


//class ResourceCache(private val context: Context) {
//    private val resourceCache = mutableMapOf<String, Int>()
//
//    fun getColorResId(resourceName: String, defType: String = "color"): Int {
//        return resourceCache.getOrPut(resourceName) {
//            context.resources.getIdentifier(resourceName, defType, context.packageName)
//        }
//    }
//}
