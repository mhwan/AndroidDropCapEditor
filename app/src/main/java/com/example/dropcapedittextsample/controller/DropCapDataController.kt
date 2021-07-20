package com.example.dropcapedittextsample.controller

import com.example.dropcapedittextsample.ArrayHashMap
import com.example.dropcapedittextsample.data.DropCapArrayMapWrapper
import com.example.dropcapedittextsample.data.ChangeDropCapProperty
import com.example.dropcapedittextsample.data.DefaultDropCapProperty
import com.example.dropcapedittextsample.data.DropCapSpanData
import com.example.dropcapedittextsample.data.creator.DropCapSpanDataFactory.createDropCapSpanData
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class DropCapDataController {
    private val dropCapArrayMap = ArrayHashMap<DropCapSpanData>()

    //적용되야할 드롭캡 데이터
    val applyDropCapData: Map<Int, DropCapSpanData>
        get() = dropCapArrayMap.dataMap
    val dropCapIndexList: List<Int>
        get() = dropCapArrayMap.dataKeys

    //지워지는 텍스트로 필요없는 드롭캡을 삭제해 반환합니다.
    fun deleteText(startIdx: Int, endIdx: Int): Set<DropCapSpanData>? {
        val startListIdx = findFirstIndexAtDropCapListByTextIndex(startIdx)
        val endListIdx = findFirstIndexAtDropCapListByTextIndex(endIdx)
        if (isOverIndex(startListIdx) || endListIdx <= startListIdx) return null
        val removeRange = startListIdx until endListIdx
        return dropCapArrayMap.removeRange(removeRange)
    }

    //텍스트가 수정된 만큼 드롭캡의 인덱스를 조정합니다.
    fun moveIndex(startIdx: Int, move: Int) {
        val startListIdx = findFirstIndexAtDropCapListByTextIndex(startIdx)
        if (isOverIndex(startListIdx)) return
        val changeRange = startListIdx until dropCapArrayMap.size
        dropCapArrayMap.changeKeyInRange(changeRange, move)
    }

    private fun isOverIndex(listIdx: Int) = listIdx >= dropCapArrayMap.size

    //새로 삽입되는 텍스트 중 문단이 있다면 드롭캡을 추가시킵니다.
    fun insertText(
        startIdx: Int,
        endIdx: Int,
        newText: CharSequence,
        defaultDropCapProperty: DefaultDropCapProperty,
    ) {
        if (dropCapArrayMap.isEmpty) {
            dropCapArrayMap.addFirst(createDropCapSpanData(defaultDropCapProperty))
        }
        val putListIdx = findFirstIndexAtDropCapListByTextIndex(startIdx)
        val tempData = LinkedHashMap<Int, DropCapSpanData>()
        var idx = startIdx
        while (idx < endIdx) {
            val nextLineIdx = newText.indexOf('\n', idx)
            if (nextLineIdx < 0 || nextLineIdx >= endIdx) break
            val dropCapIdx = nextLineIdx + 1
            tempData[dropCapIdx] = createDropCapSpanData(defaultDropCapProperty)
            idx = dropCapIdx
        }
        dropCapArrayMap.addAll(putListIdx, tempData)
    }

    //수정이 일어난 텍스트 인덱스로 수정해야할 드롭캡 리스트의 첫번째 인덱스를 찾습니다.
    private fun findFirstIndexAtDropCapListByTextIndex(textIdx: Int): Int {
        var left = 0
        var right = dropCapArrayMap.size
        while (left < right) {
            val mid = (left + right) / 2
            if (dropCapIndexList[mid] <= textIdx) left = mid + 1
            else right = mid
        }
        return left
    }

    fun changeDropCapSpanProperty(
        dropCapIdx: Int,
        changeDropCapProperty: ChangeDropCapProperty,
    ): Boolean {
        var isChange = false
        applyDropCapData[dropCapIdx]?.what?.let {
            when (changeDropCapProperty) {
                ChangeDropCapProperty.Bold -> {
                    it.isBold = !it.isBold
                    isChange = true
                }
                ChangeDropCapProperty.Italic -> {
                    it.isItalic = !it.isItalic
                    isChange = true
                }
                ChangeDropCapProperty.Underline -> {
                    it.isUnderline = !it.isUnderline
                    isChange = true
                }
                is ChangeDropCapProperty.TextSize -> {
                    if (it.textSize != changeDropCapProperty.size) {
                        it.textSize = changeDropCapProperty.size
                        isChange = true
                    }
                }
                is ChangeDropCapProperty.TextColor -> {
                    if (it.textColor != changeDropCapProperty.color) {
                        it.textColor = changeDropCapProperty.color
                        isChange = true
                    }
                }
                is ChangeDropCapProperty.Typeface -> {
                    if (it.typeface != changeDropCapProperty.typeface) {
                        it.typeface = changeDropCapProperty.typeface
                        isChange = true
                    }
                }
                is ChangeDropCapProperty.LetterSpace -> {
                    if (it.letterSpace != changeDropCapProperty.letterSpace) {
                        it.letterSpace = changeDropCapProperty.letterSpace
                        isChange = true
                    }
                }
            }
        }
        return isChange
    }

    //뷰에서 화면회전 시 parceleable로 저장한 데이터를 다시 컨트롤러에 저장합니다.
    //드롭캡은 반드시 정렬상태여야하므로, 정렬한 상태로 LinkedHashMap에 넣어 전달합니다.
    fun restoreDropCapData(arrayArrayMapWrapper: DropCapArrayMapWrapper) {
        clearDropCapData()
        val unSortedData: Map<Int, DropCapSpanData> = arrayArrayMapWrapper.map
        val sortedDataKeys = ArrayList<Int>(unSortedData.keys)
        sortedDataKeys.sort()
        val sortedData = LinkedHashMap<Int, DropCapSpanData>()
        sortedDataKeys.forEach { idx ->
            unSortedData[idx]?.let { sortedData[idx] = it }
        }
        dropCapArrayMap.addAll(0, sortedData)
    }

    fun clearDropCapData() {
        dropCapArrayMap.removeAll()
    }
}