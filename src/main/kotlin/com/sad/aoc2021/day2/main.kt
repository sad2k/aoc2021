package com.sad.aoc2021.day2

import com.sad.aoc2021.loadFromResources
import kotlin.IllegalArgumentException

private fun toAdjustment(command: String): Pair<Int, Int> {
    val (cmd, value) = command.split(" ")
    val intVal = value.toInt()
    return when (cmd) {
        "forward" -> Pair(intVal, 0)
        "down" -> Pair(0, intVal)
        "up" -> Pair(0, -intVal)
        else -> throw IllegalArgumentException("Unknown command: ${cmd}")
    }
}

data class State(val horisontalPos: Int, val depth: Int, val aim: Int)

fun main() {
    val input = loadFromResources("day2.txt").readLines()
    val adjustments = input.map(::toAdjustment)

    // part 1
    val finalPos = adjustments.reduce { acc, cmd -> Pair(acc.first + cmd.first, acc.second + cmd.second) }
    println(finalPos.first * finalPos.second)

    // part 2
    val state = State(0, 0, 0)
    val finalPos2 = adjustments.fold(state, { acc, (fwd, downUp) ->
        if (fwd == 0) {
            acc.copy(aim = acc.aim + downUp)
        } else {
            acc.copy(horisontalPos = acc.horisontalPos + fwd, depth = acc.depth + acc.aim * fwd)
        }
    })
    println(finalPos2.horisontalPos * finalPos2.depth)
}