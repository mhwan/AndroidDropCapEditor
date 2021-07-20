package com.example.dropcapedittextsample

import com.example.dropcapedittextsample.controller.DropCapDataController
import com.example.dropcapedittextsample.data.ChangeDropCapProperty
import com.example.dropcapedittextsample.data.DefaultDropCapProperty
import org.junit.After
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test

class DropCapDataControllerUnitTest {
    companion object {
        private lateinit var defaultDropCapProperty: DefaultDropCapProperty
        private val dataController: DropCapDataController = DropCapDataController()

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
    }

    @Test
    fun `빈 텍스트에 엔터없는 텍스트 삽입할 때 드롭캡을 잘 만들었는지 테스트`() {
        val text = "aaasdfsasdfasda"
        dataController.insertText(0, text.length, text, defaultDropCapProperty)

        assertEquals(1, dataController.applyDropCapData.size)
        assertEquals(true, dataController.applyDropCapData.containsKey(0))
    }

    @Test
    fun `빈 텍스트에 엔터 있는 텍스트 삽입할 때 드롭캡을 잘 만들었는지 테스트`() {
        val text = "aaasdfsasdfasda\nasdfasd\nasf\na"
        dataController.insertText(0, text.length, text, defaultDropCapProperty)

        val answer = DropCapAllPosition.getAnswer(text)

        assertEquals(answer.size, dataController.applyDropCapData.size)
        answer.forEach {
            assertEquals(true, dataController.applyDropCapData.containsKey(it))
        }
    }

    @Test
    fun `4개의 드롭캡이 있을때 첫번째 이후의 드롭캡 인덱스가 10만큼 이동하는지 테스트`() {
        val text = "aaasdfsasdfasda\nasdfasd\nasf\na"
        dataController.insertText(0, text.length, text, defaultDropCapProperty)
        dataController.moveIndex(0, 10)
        val answer = DropCapAllPosition.getAnswer(text)

        assertEquals(answer.size, dataController.applyDropCapData.size)
        for (i in 1 until answer.size) {
            assertEquals(true, dataController.applyDropCapData.containsKey(answer[i] + 10))
        }
    }

    @Test
    fun `4개의 드롭캡이 있을때 2번째 드롭캡만 지워지는지 테스트`() {
        val text = "aaasdfsasdfasda\nasdfasd\nasf\na"
        dataController.insertText(0, text.length, text, defaultDropCapProperty)
        dataController.deleteText(14, 16).also {
            assertEquals(1, it?.size)
        }

        val answer = DropCapAllPosition.getAnswer(text)
        assertEquals(answer.size - 1, dataController.applyDropCapData.size)
        for (i in 0 until answer.size) {
            if (i == 1) continue
            assertEquals(true, dataController.applyDropCapData.containsKey(answer[i]))
        }
    }

    @Test
    fun `4개의 드롭캡이 있을때 처음 드롭캡 빼고 다 지워지는지 테스트`() {
        val text = "aaasdfsasdfasda\nasdfasd\nasf\na"
        dataController.insertText(0, text.length, text, defaultDropCapProperty)
        dataController.deleteText(3, text.length).also {
            assertEquals(3, it?.size)
        }

        assertEquals(1, dataController.applyDropCapData.size)
        assertEquals(true, dataController.applyDropCapData.containsKey(0))
    }

    @Test
    fun `드롭캡의 글자 크기 속성이 바뀌는지 테스트`() {
        val text = "aaasdfsasdfasda\nasdfasd\nasf\na"
        dataController.insertText(0, text.length, text, defaultDropCapProperty)

        val newSize = 36.0f
        val changeDropCapProperty = ChangeDropCapProperty.TextSize(newSize)
        dataController.changeDropCapSpanProperty(0, changeDropCapProperty)

        assertEquals(true, dataController.applyDropCapData.containsKey(0))
        assertEquals(newSize, dataController.applyDropCapData[0]?.what?.textSize)
    }

    @Test
    fun `드롭캡의 볼드 속성이 토글되는지 테스트`() {
        val text = "aaasdfsasdfasda\nasdfasd\nasf\na"
        dataController.insertText(0, text.length, text, defaultDropCapProperty)

        val isBoldBefore = false
        val changeDropCapProperty = ChangeDropCapProperty.Bold
        dataController.changeDropCapSpanProperty(0, changeDropCapProperty)

        assertEquals(true, dataController.applyDropCapData.containsKey(0))
        assertEquals(!isBoldBefore, dataController.applyDropCapData[0]?.what?.isBold)
    }

    @After
    fun refreshDataController(){
        dataController.clearDropCapData()
    }
}