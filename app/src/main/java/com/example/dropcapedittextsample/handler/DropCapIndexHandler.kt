package com.example.dropcapedittextsample.handler

import com.example.dropcapedittextsample.controller.DropCapDataController
import com.example.dropcapedittextsample.data.DefaultDropCapProperty
import com.example.dropcapedittextsample.data.DropCapSpanData

class DropCapIndexHandler(private val controller: DropCapDataController) {
    val applyDropCapData: Map<Int, DropCapSpanData>
        get() = controller.applyDropCapData
    var removeDropCapData: Set<DropCapSpanData>? = null

    fun doBeforeTextChanged(start: Int, count: Int, after: Int) {
        removeDropCapData = controller.deleteText(start, start + count)
        controller.moveIndex(start, after - count)
    }

    fun doOnTextChanged(
        start: Int,
        count: Int,
        newText: CharSequence,
        defaultDropCapProperty: DefaultDropCapProperty
    ) {
        controller.insertText(start, start + count, newText, defaultDropCapProperty)
    }
}