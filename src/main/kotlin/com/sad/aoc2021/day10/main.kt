package com.sad.aoc2021.day10

import com.sad.aoc2021.loadFromResources

val closeToOpenMapping = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
val points = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val completionPoints = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)

private fun firstIllegalCharAndIncompletePart(s: String): Pair<Char?, List<Char>> {
    val stack = mutableListOf<Char>()
    s.forEach { ch ->
        when (ch) {
            '(', '[', '{', '<' -> stack.add(ch)
            else -> {
                val shouldOpenWith = closeToOpenMapping[ch]
                val lastOpen = stack.removeAt(stack.size - 1)
                if (shouldOpenWith != lastOpen) {
                    return (ch to stack)
                }
            }
        }
    }
    return (null to stack)
}

private fun completionScore(l: List<Char>): Long {
    var res = 0L
    for (i in l.size - 1 downTo 0) {
        res = 5 * res + completionPoints[l[i]]!!
    }
    return res
}

fun main() {
    val input = loadFromResources("day10.txt").readLines()

    // part 1
    println(input.map { firstIllegalCharAndIncompletePart(it).first }.map { points[it] ?: 0 }.sum())

    // part 2
    val incomplete = input.map(::firstIllegalCharAndIncompletePart).filter { it.first == null }.map { it.second }
    val completionScores = incomplete.map(::completionScore).sorted()
    println(completionScores[completionScores.size / 2])
}