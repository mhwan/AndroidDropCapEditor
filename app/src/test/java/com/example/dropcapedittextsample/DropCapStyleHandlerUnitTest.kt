package com.example.dropcapedittextsample

import com.example.dropcapedittextsample.controller.DropCapDataController
import com.example.dropcapedittextsample.data.ChangeDropCapProperty
import com.example.dropcapedittextsample.data.DefaultDropCapProperty
import com.example.dropcapedittextsample.handler.DropCapStyleHandler
import com.example.dropcapedittextsample.ui.dropcapview.DropCapView
import org.junit.BeforeClass
import org.junit.Test
import org.junit.Assert.*

class DropCapStyleHandlerUnitTest {
    companion object {
        private val dataController: DropCapDataController = DropCapDataController()
        private lateinit var styleHandler: DropCapStyleHandler
        private const val text =
            "asdfaasdfasdfasdfsdfasdsdfasdfasfd\n\uD83D\uDE0Dd\uD83D\uDE0Dfa\na\n"

        private object DropCapViewMock : DropCapView {
            override val textSequence: CharSequence
                get() = text

        }

        private lateinit var defaultDropCapProperty: DefaultDropCapProperty

        @BeforeClass
        @JvmStatic
        fun setUp() {
            defaultDropCapProperty = DefaultDropCapProperty(
                isBold = false,
                isItalic = true,
                isUnderline = true,
                textSize = 72.0f,
                textColor = DropCapDefaultValue.NONE_COLOR,
                letterSpace = 0.0f,
                typeface = null
            )

            dataController.insertText(0, text.length, text, defaultDropCapProperty)
            styleHandler = DropCapStyleHandler(dataController, DropCapViewMock)
        }
    }

    @Test
    fun `드롭캡 길이가 1이고, 드롭캡이 문자일때 실제 드롭캡 길이가 1인지`() {
        val len = styleHandler.getDropCapLength(0, 1)
        assertEquals(1, len)
    }

    @Test
    fun `드롭캡 길이가 1이고, 드롭캡이 이모티콘일때 실제 드롭캡 길이가 1인지`() {
        val len = styleHandler.getDropCapLength(35, 1)
        assertEquals(2, len)
    }

    @Test
    fun `드롭캡 길이가 3이고, 드롭캡이 텍스트일때 실제 드롭캡 길이가 3인지`() {
        val len = styleHandler.getDropCapLength(0, 3)
        assertEquals(3, len)
    }

    @Test
    fun `드롭캡 길이가 3이고, 드롭캡에 이모티콘 2개와 글자가 있을때 실제 드롭캡 길이가 5인지`() {
        val len = styleHandler.getDropCapLength(35, 3)
        assertEquals(5, len)
    }

    @Test
    fun `드롭캡 길이가 3인데, 문단에 텍스트가 드롭캡 길이보다 작을때 드롭캡 길이가 문단의 길이인지`() {
        val len = styleHandler.getDropCapLength(43, 3)
        assertEquals(1, len)
    }

    @Test
    fun `드롭캡이 아닌곳은 실제 드롭캡의 길이를 0인지`() {
        val len = styleHandler.getDropCapLength(3, 3)
        assertEquals(0, len)
    }

    @Test
    fun `드롭캡 길이가 1이고, 드롭캡 범위를 제대로 selection했을때 글자 크기 속성이 제대로 바뀌는지`() {
        val newSize = 87.0f
        val result = styleHandler.changeSelectionDropCapSpanProperty(
            ChangeDropCapProperty.TextSize(newSize),
            0,
            1,
            1
        )
        assertEquals(0, result)
        assertEquals(newSize, dataController.applyDropCapData[0]?.what?.textSize)
    }

    @Test
    fun `드롭캡 길이가 3이고 이모티콘이 포함될 경우, 드롭캡 범위를 제대로 selection했을때 글자 크기 속성이 제대로 바뀌는지`() {
        val newSize = 87.0f
        val result = styleHandler.changeSelectionDropCapSpanProperty(
            ChangeDropCapProperty.TextSize(newSize),
            35,
            40,
            3
        )
        assertEquals(35, result)
        assertEquals(newSize, dataController.applyDropCapData[35]?.what?.textSize)
    }

    @Test
    fun `드롭캡 길이가 3일때, 드롭캡 범위 모두 selection 하지 않을때 속성이 바뀌지 않는지`() {
        val orgSize = dataController.applyDropCapData[35]?.what?.textSize
        val result = styleHandler.changeSelectionDropCapSpanProperty(
            ChangeDropCapProperty.TextSize(178.0f),
            35,
            36,
            3
        )
        assertEquals(-1, result)
        assertEquals(orgSize, dataController.applyDropCapData[35]?.what?.textSize)
    }

    @Test
    fun `드롭캡 길이가 1일때, 아무것도 selection되지 않을때 속성이 바뀌지 않는지`() {
        val orgSize = dataController.applyDropCapData[45]?.what?.textSize
        val result = styleHandler.changeSelectionDropCapSpanProperty(
            ChangeDropCapProperty.TextSize(178.0f),
            45,
            45,
            1
        )
        assertEquals(-1, result)
        assertEquals(orgSize, dataController.applyDropCapData[45]?.what?.textSize)
    }
}