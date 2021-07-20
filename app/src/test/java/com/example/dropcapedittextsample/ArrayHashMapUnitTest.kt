package com.example.dropcapedittextsample

import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.BeforeClass

class ArrayHashMapUnitTest {
    companion object {
        private lateinit var arrayHashMap: ArrayHashMap<Int>
        private lateinit var putData: LinkedHashMap<Int, Int>

        @BeforeClass
        @JvmStatic
        fun setupArrayHashMap() {
            arrayHashMap = ArrayHashMap()
            putData = LinkedHashMap<Int, Int>()
            putData
            putData[10] = 14
            putData[12] = 25
            putData[15] = 32
        }
    }

    @Test
    fun `빈 상태에서 하나 삽입 테스트`() {
        arrayHashMap.addFirst(10)
        assertEquals(0, arrayHashMap.dataKeys[0])
        assertEquals(10, arrayHashMap.dataMap[0])
    }

    @Test
    fun `데이터가 하나 있는 상태에서 여러개 삽입 테스트`() {
        arrayHashMap.addFirst(10)
        arrayHashMap.addAll(1, putData)

        assertEquals(0, arrayHashMap.dataKeys[0])
        assertEquals(10, arrayHashMap.dataMap[0])
        assertEquals(4, arrayHashMap.size)
        val putDataKeys = putData.keys.toIntArray()
        for (i in putDataKeys.indices) {
            assertEquals(putDataKeys[i], arrayHashMap.dataKeys[i + 1])
            assertEquals(putData[putDataKeys[i]], arrayHashMap.dataMap[putDataKeys[i]])
        }
    }

    @Test
    fun `전체 데이터의 키값이 10만큼 바뀌었는지 테스트`() {
        val change = 10
        arrayHashMap.addAll(0, putData)

        arrayHashMap.changeKeyInRange(0 until putData.size, change)
        val putDataKeys = putData.keys.toIntArray()
        for (i in putDataKeys.indices) {
            assertEquals(putDataKeys[i] + change, arrayHashMap.dataKeys[i])
            assertEquals(putData[putDataKeys[i]], arrayHashMap.dataMap[putDataKeys[i] + change])
        }
    }

    @Test
    fun `중간에 있는 데이터 2개 삭제 테스트`() {
        val putData = LinkedHashMap<Int, Int>()
        putData[0] = 1
        putData[10] = 14
        putData[12] = 25
        putData[15] = 32
        arrayHashMap.addAll(0, putData)

        arrayHashMap.removeRange(1..2).also {
            assertEquals(2, it.size)
            assertEquals(true, it.contains(14))
            assertEquals(true, it.contains(25))
            assertEquals(false, it.contains(32))
        }
        assertEquals(2, arrayHashMap.size)
        assertEquals(putData[0], arrayHashMap.dataMap[0])
        assertEquals(putData[15], arrayHashMap.dataMap[15])
    }

    @Test
    fun `전체 데이터 삭제 테스트`() {
        val putData = LinkedHashMap<Int, Int>()
        putData[0] = 1
        putData[10] = 14
        putData[12] = 25
        putData[15] = 32
        arrayHashMap.addAll(0, putData)

        arrayHashMap.removeAll()
        assertEquals(true, arrayHashMap.isEmpty)

        arrayHashMap.addAll(0, putData)
        arrayHashMap.removeRange(0 until arrayHashMap.size)
        assertEquals(true, arrayHashMap.isEmpty)
    }

    @After
    fun refreshArrayHashMap(){
        arrayHashMap.removeAll()
    }
}