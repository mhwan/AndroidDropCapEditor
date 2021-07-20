package com.example.dropcapedittextsample.data

import android.graphics.Typeface

//현재 적용된 기본 드롭캡의 속성
data class DefaultDropCapProperty(
    val isBold: Boolean,
    val isItalic: Boolean,
    val isUnderline: Boolean,
    val textSize: Float,
    val textColor: Int,
    val typeface: Typeface?,
    val letterSpace: Float
)
