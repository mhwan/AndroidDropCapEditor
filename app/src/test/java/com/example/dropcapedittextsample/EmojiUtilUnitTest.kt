package com.example.dropcapedittextsample

import com.example.dropcapedittextsample.utils.EmojiUtil
import org.junit.Test
import org.junit.Assert.*

class EmojiUtilUnitTest {
    private val emojiUtil = EmojiUtil()

    @Test
    fun `이모티콘인지 아닌지 테스트`(){
        assertEquals(false, emojiUtil.isEmoji("asd"))
        assertEquals(true, emojiUtil.isEmoji("\uD83D\uDE0D"))
        assertEquals(false, emojiUtil.isEmoji("aa\uD83D\uDE0D"))
        assertEquals(false, emojiUtil.isEmoji("\uD83D\uDE0Db"))
    }

    @Test
    fun `이모티콘 길이가 맞는지 테스트`(){
        assertEquals(-1, emojiUtil.findEmojiLength("asdf"))
        assertEquals(-1, emojiUtil.findEmojiLength("a\n"))
        assertEquals(-1, emojiUtil.findEmojiLength("a\uD83D\uDC69\u200D\uD83D\uDE80"))
        assertEquals(2, emojiUtil.findEmojiLength("\uD83D\uDE0Db"))
        assertEquals(2, emojiUtil.findEmojiLength("\uD83D\uDE0D"))
        assertEquals(1, emojiUtil.findEmojiLength("♡"))
    }
}