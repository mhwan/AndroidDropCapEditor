package com.example.dropcapedittextsample

import com.example.dropcapedittextsample.controller.DropCapDataController
import com.example.dropcapedittextsample.data.DefaultDropCapProperty
import com.example.dropcapedittextsample.handler.DropCapIndexHandler
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class DropCapIndexHandlerTest {
    companion object {
        private lateinit var indexHandler: DropCapIndexHandler
        private lateinit var controller: DropCapDataController
        private lateinit var defaultDropCapProperty: DefaultDropCapProperty

        @BeforeClass
        @JvmStatic
        fun `mock 객체 만들기`() {
            defaultDropCapProperty = DefaultDropCapProperty(
                isBold = false,
                isItalic = true,
                isUnderline = true,
                textSize = 72.0f,
                textColor = DropCapDefaultValue.NONE_COLOR,
                letterSpace = 0.0f,
                typeface = null
            )
        }

        @BeforeClass
        @JvmStatic
        fun setupDropCapIndexHandler() {
            controller = DropCapDataController()
            indexHandler = DropCapIndexHandler(controller)
        }
    }

    private val text = "aassdfasdf\nasdffasdf"

    @Before
    fun insertText() {
        indexHandler.doOnTextChanged(0, text.length, text, defaultDropCapProperty)
    }

    @Test
    fun `텍스트 마지막에 엔터 삽입 테스트`() {
        val newText = text + "\n"
        indexHandler.doOnTextChanged(text.length, 1, newText, defaultDropCapProperty)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `텍스트 중간에 엔터 삽입 테스트`() {
        val insertIdx = 3
        val newText = text.substring(0, insertIdx) + "\n" + text.substring(insertIdx, text.length)
        indexHandler.doBeforeTextChanged(insertIdx, 0, 1)
        indexHandler.doOnTextChanged(insertIdx, 1, newText, defaultDropCapProperty)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `문단 앞에 엔터 삽입 테스트`() {
        val insertIdx = 0
        val newText = "\n" + text.substring(insertIdx, text.length)
        indexHandler.doBeforeTextChanged(insertIdx, 0, 1)
        indexHandler.doOnTextChanged(insertIdx, 1, newText, defaultDropCapProperty)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `문단 중간에 텍스트 삽입 테스트`() {
        val insertIdx = 3
        val newText = text.substring(0, insertIdx) + "a" + text.substring(insertIdx, text.length)
        indexHandler.doBeforeTextChanged(insertIdx, 0, 1)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `문단 마지막에 텍스트 삽입 테스트`() {
        val newText = text + "s"
        indexHandler.doOnTextChanged(text.length, 1, newText, defaultDropCapProperty)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `문단 앞에 텍스트 삽입 테스트`() {
        val insertIdx = 0
        val newText = "as" + text.substring(insertIdx, text.length)
        indexHandler.doBeforeTextChanged(insertIdx, 0, 2)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `문단의 맨 앞을 지울때 문단의 드롭캡이 제대로 지워지는지 테스트`() {
        val deleteIdx = 10
        val newText = text.substring(0, deleteIdx) + text.substring(deleteIdx + 1, text.length)
        indexHandler.doBeforeTextChanged(deleteIdx, 1, 0)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `드롭캡 부분을 지울때 드롭캡의 자리가 유지되는지 테스트`() {
        val deleteIdx = 11
        val newText = text.substring(0, deleteIdx) + text.substring(deleteIdx + 1, text.length)
        indexHandler.doBeforeTextChanged(deleteIdx, 1, 0)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `문단의 중간을 지울때 테스트`() {
        val deleteIdx = 13
        val newText = text.substring(0, deleteIdx) + text.substring(deleteIdx + 5, text.length)
        indexHandler.doBeforeTextChanged(deleteIdx, 5, 0)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `텍스트 중간에 엔터가 포함된 텍스트 삽입 테스트`() {
        val insertText = "asdfasdf\nasdf\nasdjf\naaa"
        val insertIdx = 3
        val newText =
            text.substring(0, insertIdx) + insertText + text.substring(insertIdx, text.length)
        indexHandler.doBeforeTextChanged(insertIdx, 0, insertText.length)
        indexHandler.doOnTextChanged(insertIdx, insertText.length, newText, defaultDropCapProperty)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `텍스트 중간에 여러 문단을 지우는 테스트`() {
        val deleteIdx = 3
        val deleteLen = 10
        val newText =
            text.substring(0, deleteIdx) + text.substring(deleteIdx + deleteLen, text.length)
        indexHandler.doBeforeTextChanged(deleteIdx, deleteLen, 0)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `텍스트 여러 문단을 다른 문단의 텍스트로 대체하는 테스트`() {
        val deleteIdx = 3
        val deleteLen = 10
        val insertText = "asdfasdf\nasdf\nasdjf\naaa"
        val newText =
            text.substring(0, deleteIdx) + insertText + text.substring(deleteIdx + deleteLen,
                text.length)
        indexHandler.doBeforeTextChanged(deleteIdx, deleteLen, insertText.length)
        indexHandler.doOnTextChanged(deleteIdx, insertText.length, newText, defaultDropCapProperty)

        val answer = DropCapAllPosition.getAnswer(newText)

        assertEquals(answer.size, indexHandler.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, indexHandler.applyDropCapData.containsKey(it))
        }
    }

    @After
    fun refreshDataController() {
        controller.clearDropCapData()
    }
}