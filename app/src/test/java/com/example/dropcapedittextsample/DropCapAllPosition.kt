package com.example.dropcapedittextsample

object DropCapAllPosition {
    fun getAnswer(chars: CharSequence): ArrayList<Int> {
        val correctPositions = ArrayList<Int>()
        correctPositions.add(0)

        var idx = 0
        while (idx < chars.length) {
            val nextLineIdx = chars.indexOf('\n', idx)
            if (nextLineIdx < 0) {
                break
            }

            correctPositions.add(nextLineIdx + 1)
            idx = nextLineIdx + 1
        }

        return correctPositions
    }
}