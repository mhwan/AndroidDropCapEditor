package com.example.dropcapedittextsample.handler

import com.example.dropcapedittextsample.data.ChangeDropCapProperty
import com.example.dropcapedittextsample.controller.DropCapDataController
import com.example.dropcapedittextsample.ui.dropcapview.DropCapView
import com.example.dropcapedittextsample.utils.EmojiUtil
import kotlin.math.max
import kotlin.math.min

class DropCapStyleHandler(
    private val controller: DropCapDataController,
    private val dropCapView: DropCapView
) {
    private val paragraphStartIndex: List<Int>
        get() = controller.dropCapIndexList
    private val paragraphEndIndex: List<Int>
        get() = paragraphStartIndex.filterIndexed { index, _ -> (index > 0) }.map { it - 1 }
            .toMutableList().apply { add(dropCapView.textSequence.length) }
    private val emojiUtil = EmojiUtil()

    fun changeSelectionDropCapSpanProperty(
        changeDropCapProperty: ChangeDropCapProperty,
        selectionStart: Int,
        selectionEnd: Int,
        dropCapLength: Int
    ): Int {
        val dropCapIdx =
            getDropCapIndexIfCorrectSelection(selectionStart, selectionEnd, dropCapLength)
        if (dropCapIdx < 0) return -1
        val isChange = controller.changeDropCapSpanProperty(dropCapIdx, changeDropCapProperty)
        return if (isChange) dropCapIdx else -1
    }

    //현재 선택된 영역이 드롭캡 영역인지 판단합니다.
    private fun getDropCapIndexIfCorrectSelection(
        selectionStart: Int,
        selectionEnd: Int,
        dropCapLength: Int
    ): Int {
        val selStart = getSelectionStart(selectionStart, selectionEnd)
        val selEnd = getSelectionEnd(selectionStart, selectionEnd)
        val selLength = getDropCapLength(selStart, dropCapLength)

        if (selLength >= dropCapLength
            && selEnd - selStart == selLength
            && isSelectionStartDropCap(selStart)
        ) {
            return selStart
        }

        return -1
    }

    //해당 드롭캡 인덱스에 이모티콘이 있을 경우를 포함해 드롭캡이 적용되야할 실제 텍스트 길이를 반환합니다.
    fun getDropCapLength(dropCapIdx: Int, dropCapLength: Int): Int {
        val text = dropCapView.textSequence
        val paragraphLength = getParagraphLength(dropCapIdx)
        val endIdx = dropCapIdx + paragraphLength
        var realLength = 0 //실제 반환해야할 드롭캡 길이 (이모티콘은 이모티콘 길이만큼, 글자는 1글자)
        var startIdx = dropCapIdx //charSequence로 자를 start 인덱스
        var count = 0
        while (count++ < dropCapLength && startIdx < endIdx) {
            val emojiLength =
                emojiUtil.findEmojiLength(text.subSequence(startIdx, endIdx))

            val length = if (emojiLength > 0) emojiLength else 1

            startIdx += length
            realLength += length
        }

        return realLength
    }

    //드롭캡이 있는 해당 문단의 길이를 반환합니다.
    private fun getParagraphLength(dropCapIdx: Int): Int {
        val paragraphIdx = paragraphStartIndex.indexOf(dropCapIdx)
        if (paragraphIdx < 0) return -1
        val paragraphEndIndex = paragraphEndIndex[paragraphIdx]
        return paragraphEndIndex - dropCapIdx
    }

    private fun isSelectionStartDropCap(idx: Int) = controller.applyDropCapData.containsKey(idx)

    private fun getSelectionStart(selectionStart: Int, selectionEnd: Int) =
        min(selectionStart, selectionEnd)

    private fun getSelectionEnd(selectionStart: Int, selectionEnd: Int) =
        max(selectionStart, selectionEnd)
}