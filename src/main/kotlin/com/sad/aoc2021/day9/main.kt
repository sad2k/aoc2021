package com.sad.aoc2021.day9

import com.sad.aoc2021.loadFromResources

fun findAdjacent(i: Int, j: Int, input: List<List<Int>>): List<Pair<Int, Int>> {
    return arrayOf(
        Pair(i - 1, j), // up
        Pair(i + 1, j), // down
        Pair(i, j - 1), // left
        Pair(i, j + 1) // right
    ).filter { (row, col) ->
        row >= 0 && row < input.size && col >= 0 && col < input[0].size
    }
}

fun findLowPoints(input: List<List<Int>>): List<Pair<Int, Int>> {
    var res = mutableListOf<Pair<Int, Int>>()
    for (i in input.indices) {
        val row = input[i]
        for (j in row.indices) {
            val digit = row[j]
            val adjacent = findAdjacent(i, j, input).map { (row, col) ->
                input[row][col]
            }
            if (adjacent.all { it > digit }) {
                res.add(i to j)
            }
        }
    }
    return res
}

fun findBasins(input: List<List<Int>>, lowPoints: List<Pair<Int, Int>>): List<Int> {
    fun exploreBasin(row: Int, col: Int, basin: MutableSet<Pair<Int, Int>>) {
        basin.add(row to col)
        val thisVal = input[row][col]
        val adjacent = findAdjacent(row, col, input)
        for (adj in adjacent) {
            if (!(adj in basin)) {
                val thatVal = input[adj.first][adj.second]
                if (thatVal != 9 && thatVal > thisVal) {
                    exploreBasin(adj.first, adj.second, basin)
                }
            }
        }
    }

    return lowPoints.map { (row, col) ->
        val s = mutableSetOf<Pair<Int, Int>>()
        exploreBasin(row, col, s)
        s.size
    }
}


fun main() {
    val input = loadFromResources("day9.txt").readLines().map { it.map { ch -> ch.digitToInt() } }

    // part 1
    val lowPoints = findLowPoints(input)
    println(lowPoints.map { (row, col) -> input[row][col] + 1 }.sum())

    // part 2
    println(findBasins(input, lowPoints).sortedDescending().take(3).reduce{ a, b -> a*b })
}


