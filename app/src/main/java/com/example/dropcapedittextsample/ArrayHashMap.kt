package com.example.dropcapedittextsample

import com.example.dropcapedittextsample.utils.removeRange

class ArrayHashMap<V> {
    private val _dataKeys = mutableListOf<Int>()
    private val _dataMap = mutableMapOf<Int, V>()
    val dataKeys: List<Int>
        get() = _dataKeys
    val dataMap: Map<Int, V>
        get() = _dataMap
    val size: Int
        get() = dataKeys.size
    val isEmpty: Boolean
        get() = dataKeys.isEmpty() && dataMap.isEmpty()

    fun addAll(putListIdx: Int, dataMap: LinkedHashMap<Int, V>) {
        dataMap.forEach { (key, data) ->
            _dataMap[key] = data
        }
        _dataKeys.addAll(putListIdx, dataMap.keys)
    }

    fun addFirst(data: V) {
        _dataKeys.add(0, 0)
        _dataMap[0] = data
    }

    fun changeKeyInRange(listRange: IntRange, change: Int) {
        val tempMap = mutableMapOf<Int, V>()
        for (i in listRange) {
            val before = _dataKeys[i]
            _dataMap.remove(before)?.let {
                val after = before + change
                tempMap[after] = it
                _dataKeys[i] = after
            }
        }
        _dataMap.putAll(tempMap)
    }

    fun removeRange(listRange: IntRange): Set<V> {
        val removeSet = mutableSetOf<V>()
        for (i in listRange) {
            _dataMap.remove(_dataKeys[i])?.let {
                removeSet.add(it)
            }
        }
        _dataKeys.removeRange(listRange)
        return removeSet
    }

    fun removeAll() {
        _dataKeys.clear()
        _dataMap.clear()
    }
}