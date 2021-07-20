package com.example.dropcapedittextsample.data

sealed class ChangeDropCapProperty {
    object Bold : ChangeDropCapProperty()
    object Italic : ChangeDropCapProperty()
    object Underline : ChangeDropCapProperty()
    data class TextSize(val size: Float) : ChangeDropCapProperty()
    data class TextColor(val color: Int) : ChangeDropCapProperty()
    data class Typeface(val typeface: android.graphics.Typeface) : ChangeDropCapProperty()
    data class LetterSpace(val letterSpace: Float) : ChangeDropCapProperty()
}