package com.example.dropcapedittextsample.data

import android.os.Parcelable
import android.text.Spanned
import com.example.dropcapedittextsample.ui.span.DropCapSpan
import kotlinx.parcelize.Parcelize

@Parcelize
data class DropCapSpanData(
    val what: DropCapSpan,
    val flag: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
) : Parcelable

data class WatcherSpanData(val what: Any, val start: Int, val end: Int, val flag: Int)