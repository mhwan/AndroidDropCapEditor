package com.example.dropcapedittextsample.utils

inline fun <reified T> MutableList<T>.removeRange(range: IntRange) {
    val startIdx = range.first
    val endIdx = range.last
    if (startIdx >= size) {
        throw IndexOutOfBoundsException("fromIndex $startIdx >= size $size")
    }
    if (endIdx > size) {
        throw IndexOutOfBoundsException("toIndex $endIdx > size $size")
    }
    if (startIdx > endIdx) {
        throw IndexOutOfBoundsException("fromIndex $startIdx > toIndex $endIdx")
    }
    val filtered = filterIndexed { i, _ -> i < startIdx || i > endIdx }
    clear()
    addAll(filtered)
}
