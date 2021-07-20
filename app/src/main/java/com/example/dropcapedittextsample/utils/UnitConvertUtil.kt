package com.example.dropcapedittextsample.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

object UnitConvertUtil {
    fun spToPx(context: Context, sp: Number): Float {
        context.resources.displayMetrics.let { dm ->
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), dm)
        }
    }

    fun getPxSize(context: Context, size: Number, isSP: Boolean) =
        if (isSP) spToPx(context, size) else size.toFloat()
}