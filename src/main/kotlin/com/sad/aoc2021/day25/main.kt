package com.sad.aoc2021.day25

import com.sad.aoc2021.loadFromResources

enum class MoveState {
    NOTHING, MOVE_FROM, MOVE_TO
}

fun move(input: List<String>): Pair<List<String>, Boolean> {
    var updated = false
    val res = mutableListOf<MutableList<Char>>()
    // moves east
    for (line in input) {
        val moveFlags = MutableList(line.length) { MoveState.NOTHING }
        line.forEachIndexed { i, ch ->
            val nextI = if (i == line.length - 1) {
                0
            } else {
                i + 1
            }
            if (line[i] == '>' && line[nextI] == '.') {
                moveFlags[i] = MoveState.MOVE_FROM
                moveFlags[nextI] = MoveState.MOVE_TO
                updated = true
            }
        }
        val newLine = line.indices.map { i ->
            if (moveFlags[i] == MoveState.MOVE_FROM) {
                '.'
            } else if (moveFlags[i] == MoveState.MOVE_TO) {
                '>'
            } else {
                line[i]
            }
        }.toMutableList()
        res.add(newLine)
    }
    // moves down
    for (i in res[0].indices) {
        val moveFlags = MutableList(res.size) { MoveState.NOTHING }
        for (j in res.indices) {
            val nextJ = if (j == res.size - 1) {
                0
            } else {
                j + 1
            }
            if (res[j][i] == 'v' && res[nextJ][i] == '.') {
                moveFlags[j] = MoveState.MOVE_FROM
                moveFlags[nextJ] = MoveState.MOVE_TO
                updated = true
            }
        }
        for (j in moveFlags.indices) {
            val flag = moveFlags[j]
            if (flag == MoveState.MOVE_FROM) {
                res[j][i] = '.'
            } else if (flag == MoveState.MOVE_TO) {
                res[j][i] = 'v'
            }
        }
    }
    return Pair(res.map { it.joinToString(separator = "") }, updated)
}

fun moveWhilePossible(input: List<String>): Int {
    var current = input.toMutableList()
    var updated = true
    var i = 0
    while (updated) {
        i++
        val pair = move(current)
        current = pair.first.toMutableList()
        updated = pair.second
    }
    return i
}

fun main() {
    val input = loadFromResources("day25.txt").readLines()
    println(moveWhilePossible(input))
}