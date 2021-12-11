package com.sad.aoc2021.day11

import com.sad.aoc2021.loadFromResources

fun findAdjacent(i: Int, j: Int, input: List<List<Int>>): List<Pair<Int, Int>> {
    return arrayOf(
        Pair(i - 1, j), // up
        Pair(i + 1, j), // down
        Pair(i, j - 1), // left
        Pair(i, j + 1), // right
        Pair(i + 1, j + 1),
        Pair(i + 1, j - 1),
        Pair(i - 1, j + 1),
        Pair(i - 1, j - 1)
    ).filter { (row, col) ->
        row >= 0 && row < input.size && col >= 0 && col < input[0].size
    }
}

fun light(input: MutableList<MutableList<Int>>, i: Int, j: Int) {
    input[i][j] = 0
    val adjacent = findAdjacent(i, j, input)
    for ((adjr, adjc) in adjacent) {
        if (input[adjr][adjc] != 0) {
            if (input[adjr][adjc] != 10) {
                input[adjr][adjc] = input[adjr][adjc] + 1
            }
            if (input[adjr][adjc] == 10) {
                light(input, adjr, adjc)
            }
        }
    }
}

fun simulateStep(input: List<List<Int>>): List<List<Int>> {
    // increase by 1
    val mutableMap = input.map {
        it.map {
            it + 1
        }.toMutableList()
    }.toMutableList()
    for (i in mutableMap.indices) {
        val row = mutableMap[i]
        for (j in row.indices) {
            if (mutableMap[i][j] == 10) {
                light(mutableMap, i, j)
            }
        }
    }
    return mutableMap
}

fun simulateSteps(input: List<List<Int>>, steps: Int): Pair<Int, List<List<Int>>> {
    var map = input
    var lights = 0
    for (i in 1..steps) {
        map = simulateStep(map)
        lights += map.map { it.map { v -> if (v == 0) 1 else 0 }.sum() }.sum()
    }
    return (lights to map)
}

fun findSynchronizationMoment(input: List<List<Int>>): Int {
    var i = 0
    var map = input
    do {
        i += 1
        map = simulateStep(map)
    } while (!(map.all { it.all { v -> v == 0 } }))
    return i
}

fun main() {
    val input = loadFromResources("day11.txt").readLines().map {
        it.map { ch ->
            ch.digitToInt()
        }
    }
//    val after1step = simulateStep(simulateStep(input))
//    println(after1step.map { it.joinToString(separator = "") }.joinToString(separator = "\n"))

    // part 1
    println(simulateSteps(input, 100).first)

    // part 2
    println(findSynchronizationMoment(input))
}