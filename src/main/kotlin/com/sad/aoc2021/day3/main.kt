package com.sad.aoc2021.day3

import com.sad.aoc2021.loadFromResources
import java.lang.RuntimeException

private fun calculateDigitsInPos(input: List<String>): List<List<Int>> {
    return input.map {
        it.map {
            it.digitToInt()
        }
    }
}

private fun digitDistribution(input: List<List<Int>>, pos: Int): Map<Int, Int> {
    val digits = input.map { it[pos] }
    return digits.groupingBy { it }.eachCount()
}

private fun aggregateDigits(input: List<List<Int>>): List<Map<Int, Int>> {
    val numPos = input[0].size
    return (0 until numPos).map { i ->
        digitDistribution(input, i)
    }
}

private fun filterDigits(input: List<List<Int>>, filterBuilder: (Map<Int, Int>) -> Int): List<Int> {
    var workingInput = input.map { it.toList() }
    for (i in input[0].indices) {
        val digits = digitDistribution(workingInput, i)
        val filteringDigit = filterBuilder(digits)
        workingInput = workingInput.filter { it[i] == filteringDigit }
        if (workingInput.size == 1) {
            return workingInput[0]
        }
    }
    throw RuntimeException("couldn't find a value!")
}

fun main() {
    val input = loadFromResources("day3.txt").readLines()
    val digitsInPos = calculateDigitsInPos(input)
    val aggDigitsInPos = aggregateDigits(digitsInPos)

    // part 1
    val mostCommon = aggDigitsInPos.map {
        val zeros = it.getOrDefault(0, 0)
        val ones = it.getOrDefault(1, 0)
        if (zeros > ones) 0 else 1
    }
    val mostCommonStr = mostCommon.joinToString(separator = "")
    val mostCommonParsed = Integer.parseInt(mostCommonStr, 2)
    val leastCommon = mostCommonStr.map { if (it == '0') 1 else 0 }
    val leastCommonStr = leastCommon.joinToString(separator = "")
    val leastCommonParsed = Integer.parseInt(leastCommonStr, 2)
    println(mostCommonParsed * leastCommonParsed)

    // part 2
    val oxygenGenRating = Integer.parseInt(filterDigits(digitsInPos) {
        if (it.getOrDefault(0, 0) > it.getOrDefault(
                1,
                0
            )
        ) 0 else 1
    }.joinToString(separator = ""), 2)
    val co2ScrubberRating = Integer.parseInt(filterDigits(digitsInPos) {
        if (it.getOrDefault(0, 0) > it.getOrDefault(
                1,
                0
            )
        ) 1 else 0
    }.joinToString(separator = ""), 2)
    println(oxygenGenRating * co2ScrubberRating)
}


