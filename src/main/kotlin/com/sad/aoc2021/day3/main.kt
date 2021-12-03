package com.sad.aoc2021.day3

import com.sad.aoc2021.loadFromResources

private fun calculateDigitsInPos(input: List<String>): List<List<Int>> {
    return input.map {
        it.map {
            it.digitToInt()
        }
    }
}

private fun aggregateDigits(input: List<List<Int>>): List<Map<Int, Int>> {
    val numPos = input[0].size
    return (0 until numPos).map { i ->
        val digits = input.map { it[i] }
        digits.groupingBy { it }.eachCount()
    }
}


fun main() {
    val input = loadFromResources("day3.txt").readLines()
    val digitsInPos = aggregateDigits(calculateDigitsInPos(input))
    val mostCommon = digitsInPos.map {
        val zeros = it.getOrDefault(0, 0)
        val ones = it.getOrDefault(1, 0)
        if (zeros > ones) 0 else 1
    }.joinToString(separator = "")
    val mostCommonParsed = Integer.parseInt(mostCommon, 2)
    val leastCommon = mostCommon.map { if (it == '0') '1' else '0' }.joinToString(separator = "")
    val leastCommonParsed = Integer.parseInt(leastCommon, 2)
    println(mostCommonParsed * leastCommonParsed)
}


