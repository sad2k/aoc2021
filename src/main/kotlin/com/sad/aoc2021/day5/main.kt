package com.sad.aoc2021.day5

import com.sad.aoc2021.loadFromResources
import java.lang.StringBuilder

fun populateMap(parsed: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, map: Array<IntArray>, supportDiags: Boolean) {
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
            if (supportDiags) {
                val xstep: Int
                val ystep: Int
                val xpred: (Int, Int) -> Boolean
                val ypred: (Int, Int) -> Boolean
                if (x1 < x2) {
                    xstep = 1
                    xpred = { curx: Int, lastx: Int -> curx <= lastx }
                } else {
                    xstep = -1
                    xpred = { curx: Int, lastx: Int -> curx >= lastx }
                }
                if (y1 < y2) {
                    ystep = 1
                    ypred = { cury: Int, lasty: Int -> cury <= lasty }
                } else {
                    ystep = -1
                    ypred = { cury: Int, lasty: Int -> cury >= lasty }
                }
                var x = x1
                var y = y1
                while (xpred(x, x2) && ypred(y, y2)) {
                    map[y][x] = map[y][x] + 1
                    x += xstep
                    y += ystep
                }
            }
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

    // part 1
    val map1 = Array(ymax + 1) { IntArray(xmax + 1) }
    populateMap(parsed, map1, false)
//    printMap(map1)
    println(calculateNumberOfOverlaps(map1))

    // part 2
    val map2 = Array(ymax + 1) { IntArray(xmax + 1) }
    populateMap(parsed, map2, true)
//    printMap(map2)
    println(calculateNumberOfOverlaps(map2))
}


