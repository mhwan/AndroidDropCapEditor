package com.example.dropcapedittextsample.data.creator

import com.example.dropcapedittextsample.DropCapDefaultValue
import com.example.dropcapedittextsample.data.DefaultDropCapProperty
import com.example.dropcapedittextsample.data.DropCapSpanData
import com.example.dropcapedittextsample.ui.span.DropCapSpan

object DropCapSpanDataFactory {
    fun createDropCapSpanData(defaultDropCapProperty: DefaultDropCapProperty): DropCapSpanData {
        val dropCapSpan = DropCapSpan().apply {
            textSize = defaultDropCapProperty.textSize
            isBold = defaultDropCapProperty.isBold
            isItalic = defaultDropCapProperty.isItalic
            isUnderline = defaultDropCapProperty.isUnderline
            if (defaultDropCapProperty.textColor != DropCapDefaultValue.NONE_COLOR) {
                textColor = defaultDropCapProperty.textColor
            }
            defaultDropCapProperty.typeface?.let {
                typeface = it
            }
            letterSpace = defaultDropCapProperty.letterSpace
        }
        return DropCapSpanData(dropCapSpan, DropCapDefaultValue.DROP_CAP_FLAG)
    }
}