package com.sad.aoc2021.day13

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.splitWhen
import java.lang.IllegalArgumentException

private fun buildMap(input: List<String>): Array<Array<Boolean>> {
    val dots = input.map { it.split(",").map { s -> s.toInt() } }
    val maxx = dots.map { it[0] }.maxOrNull()!!
    val maxy = dots.map { it[1] }.maxOrNull()!!
    val map = Array(maxy + 1) { Array(maxx + 1) { false } }
    dots.forEach {
        val x = it[0]
        val y = it[1]
        map[y][x] = true
    }
    return map
}

private fun mergeRow(from1: Array<Boolean>, from2: Array<Boolean>, to: Array<Boolean>) {
    if (from1.size != from2.size) {
        throw IllegalArgumentException("from1.size != from2.size")
    }
    if (from1.size != to.size) {
        throw IllegalArgumentException("from1.size != to.size")
    }
    for (i in from1.indices) {
        to[i] = from1[i] || from2[i]
    }
}

private fun fold(map: Array<Array<Boolean>>, acrossY: Boolean): Array<Array<Boolean>> {
    if (acrossY) {
        val newMap = Array((map.size - 1) / 2) { Array(map[0].size) { false } }
        for (i in newMap.indices) {
            mergeRow(map[i], map[map.size - 1 - i], newMap[i])
        }
        return newMap
    } else {
        val newMap = Array(map.size) { Array((map[0].size - 1) / 2) { false } }
        for (i in newMap[0].indices) {
            for (j in newMap.indices) {
                newMap[j][i] = map[j][i] || map[j][map[0].size - 1 - i]
            }
        }
        return newMap
    }
}

private fun countDots(map: Array<Array<Boolean>>): Int {
    fun boolToInt(b: Boolean): Int = if (b) 1 else 0

    return map.map {
        it.sumOf(::boolToInt)
    }.sum()
}

private fun printMap(map: Array<Array<Boolean>>) {
    map.forEach {
        println(it.map { b -> if (b) '#' else '.' }.joinToString(separator = ""))
    }
}

fun main() {
    val input = loadFromResources("day13.txt").readLines().splitWhen { it.trim().isEmpty() }
    val dots = input[0]
    val folds = input[1]
    val map = buildMap(dots)

    // part 1
    val folded = fold(map, folds[0].contains("fold along y"))
    println(countDots(folded))
//    println(printMap(fold(fold(map, true), false)))
}