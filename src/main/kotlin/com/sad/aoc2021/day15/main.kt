package com.sad.aoc2021.day15

import com.sad.aoc2021.loadFromResources

private fun traverse(map: List<List<Int>>): List<List<Int>> {
    val risk = MutableList(map.size) { MutableList(map[0].size) { 0 } }
    for (i in map.indices) {
        val row = map[i]
        for (j in row.indices) {
            if (i == 0 && j == 0) {
                risk[i][j] = 0
            } else {
                var bestPathSoFar = Int.MAX_VALUE
                if (i > 0 && risk[i - 1][j] < bestPathSoFar) {
                    bestPathSoFar = risk[i - 1][j]
                }
                if (j > 0 && risk[i][j - 1] < bestPathSoFar) {
                    bestPathSoFar = risk[i][j - 1]
                }
                risk[i][j] = map[i][j] + bestPathSoFar
            }
        }
    }
    return risk
}

fun main() {
    val input = loadFromResources("day15.txt").readLines().map {
        it.map { ch ->
            ch.digitToInt()
        }
    }
    val risks = traverse(input)
    println(risks[risks.size - 1][risks[0].size - 1])
}