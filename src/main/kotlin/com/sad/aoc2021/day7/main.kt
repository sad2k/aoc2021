package com.sad.aoc2021.day7

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.readFirstLine

private fun alignmentCost(input: List<Int>, pos: Int) = input.map { Math.abs(it-pos) }.sum()

private fun findBestAlignment(input: List<Int>): Pair<Int, Int> {
    val maxPos = input.maxOrNull()!!
    var bestCost: Int? = null
    var bestPos: Int? = null
    for (i in 0..maxPos) {
        val cost = alignmentCost(input, i)
        if (bestCost == null || cost < bestCost) {
            bestCost = cost
            bestPos = i
        }
    }
    return Pair(bestPos!!, bestCost!!)
}

fun main() {
    val input = loadFromResources("day7.txt").readFirstLine().split(",").map(String::toInt)
    println(findBestAlignment(input))
}


