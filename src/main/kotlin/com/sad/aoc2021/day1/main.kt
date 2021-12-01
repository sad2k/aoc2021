package com.sad.aoc2021.day1

import com.sad.aoc2021.loadFromResources

private fun countIncreases(xs: List<Int>): Int {
    val previous = listOf(xs[0]) + xs
    return (xs zip previous).map { it.first - it.second }.filter { it > 0 }.size
}

fun main() {
    val input: List<Int> = loadFromResources("day1.txt").readLines().map { it.toInt() }

    // part 1
    println(countIncreases(input))

    // part 2
    println(countIncreases(input.windowed(size = 3, step = 1).map(List<Int>::sum)))
}