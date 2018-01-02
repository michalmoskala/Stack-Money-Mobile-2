package com.example.zbyszek.stackmoney2.model

import android.content.Context
import android.graphics.Typeface


object FontManager {

    val ROOT = "fonts/"
    val FONTAWESOME = ROOT + "fontawesome-webfont.ttf"

    fun getTypeface(context: Context, font: String): Typeface {
        return Typeface.createFromAsset(context.getAssets(), font)
    }

}