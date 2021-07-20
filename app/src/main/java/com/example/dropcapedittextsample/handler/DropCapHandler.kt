package com.example.dropcapedittextsample.handler

import com.example.dropcapedittextsample.controller.DropCapDataController
import com.example.dropcapedittextsample.data.DropCapArrayMapWrapper
import com.example.dropcapedittextsample.ui.dropcapview.DropCapView

class DropCapHandler(dropCapView: DropCapView) {
    private val controller = DropCapDataController()
    val indexHandler: DropCapIndexHandler = DropCapIndexHandler(controller)
    val styleHandler: DropCapStyleHandler = DropCapStyleHandler(controller, dropCapView)

    fun restoreDropCapData(arrayMapWrapper: DropCapArrayMapWrapper){
        controller.restoreDropCapData(arrayMapWrapper)
    }

    fun getDropCapDataWrapper() = DropCapArrayMapWrapper(controller.applyDropCapData)
}