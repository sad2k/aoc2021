package com.sad.aoc2021.day6

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.readFirstLine

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
    return (1..days).fold(fishes) {
        acc, _ ->
            executeOneDay(acc)
    }
}

fun main() {
    val input = loadFromResources("day6.txt").readFirstLine().split(",").map(String::toInt)
    println(executeDays(input, 80).size)
}