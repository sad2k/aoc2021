package com.sad.aoc2021.day6

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.readFirstLine

val cache = mutableMapOf<Pair<Int,Int>, Long>()

private fun executeOneDay(fishes: List<Int>): List<Int> {
    return fishes.flatMap { fish ->
        if (fish == 0) {
            listOf(6, 8)
        } else {
            listOf(fish - 1)
        }
    }
}

private fun executeDays(fishes: List<Int>, days: Int): List<Int> {
    return (1..days).fold(fishes) { acc, _ ->
        executeOneDay(acc)
    }
}

private fun modelFish(timer: Int, days: Int): Long {
    val cached = cache[timer to days]
    if (cached != null) {
        return cached
    }

    var res = 1L
    var curDays = days - timer - 1
    while (curDays >= 0) {
        res += modelFish(8, curDays)
        curDays -= 7
    }
    cache.put(timer to days, res)
    return res
}

private fun executeDays2(fishes: List<Int>, days: Int): Long {
    return fishes.map { modelFish(it, days) }.sum()
}

fun main() {
    val input = loadFromResources("day6.txt").readFirstLine().split(",").map(String::toInt)
    println(executeDays2(input, 80))
    println(executeDays2(input, 256))
}