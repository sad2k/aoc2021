package com.sad.aoc2021.day5

import com.sad.aoc2021.loadFromResources
import java.lang.StringBuilder

fun populateMap(parsed: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, map: Array<IntArray>) {
    for (pair in parsed) {
        val (x1, y1) = pair.first
        val (x2, y2) = pair.second
        if (x1 == x2) {
            if (y1 < y2) {
                for (y in y1..y2) {
                    map[y][x1] = map[y][x1] + 1
                }
            } else {
                for (y in y1 downTo y2) {
                    map[y][x1] = map[y][x1] + 1
                }
            }
        } else if (y1 == y2) {
            if (x1 < x2) {
                for (x in x1..x2) {
                    map[y1][x] = map[y1][x] + 1
                }
            } else {
                for (x in x1 downTo x2) {
                    map[y1][x] = map[y1][x] + 1
                }
            }
        } else {
            // not supported
        }
    }
}

private fun printMap(map: Array<IntArray>) {
    for (row in map) {
        val sb = StringBuilder()
        for (i in row) {
            if (i == 0) {
                sb.append('.')
            } else {
                sb.append(i)
            }
        }
        println(sb)
    }
}

private fun calculateNumberOfOverlaps(map: Array<IntArray>): Int {
    return map.fold(0) {
        acc, row ->
            acc + row.filter { it >= 2 }.count()
    }
}

fun main() {
    val input = loadFromResources("day5.txt").readLines()
    val parsed = input.map {
        val (left, right) = it.split(" -> ")
        val (x1, y1) = left.split(",").map(String::toInt)
        val (x2, y2) = right.split(",").map(String::toInt)
        Pair(Pair(x1, y1), Pair(x2, y2))
    }
    val xmax = parsed.fold(0) { acc, pair -> Math.max(Math.max(acc, pair.first.first), pair.second.first) }
    val ymax = parsed.fold(0) { acc, pair -> Math.max(Math.max(acc, pair.first.second), pair.second.second) }
    val map = Array(ymax + 1) { IntArray(xmax + 1) }
    populateMap(parsed, map)
//    printMap(map)
    println(calculateNumberOfOverlaps(map))
}


