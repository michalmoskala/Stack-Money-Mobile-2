package com.example.zbyszek.stackmoney2.model

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView.BufferType
import android.widget.TextView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


class RialTextView : TextView {

    internal var rawText: String = ""

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun setText(text: CharSequence, type: BufferType) {
        rawText = text.toString()
        var prezzo = text.toString()
        try {

            val symbols = DecimalFormatSymbols()
            symbols.setDecimalSeparator(',')
            val decimalFormat = DecimalFormat("###,###,###,###", symbols)
            prezzo = decimalFormat.format(Integer.parseInt(text.toString()))
        } catch (e: Exception) {
        }

        super.setText(prezzo, type)
    }

    override fun getText(): CharSequence {

        return rawText
    }
}