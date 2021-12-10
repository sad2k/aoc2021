package com.sad.aoc2021.day10

import com.sad.aoc2021.loadFromResources

val closeToOpenMapping = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
val points = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

private fun firstIllegalChar(s: String): Char? {
    val stack = mutableListOf<Char>()
    s.forEach { ch ->
        when (ch) {
            '(', '[', '{', '<' -> stack.add(ch)
            else -> {
                val shouldOpenWith = closeToOpenMapping[ch]
                val lastOpen = stack.removeAt(stack.size - 1)
                if (shouldOpenWith != lastOpen) {
                    return ch
                }
            }
        }
    }
    return null
}

fun main() {
    val input = loadFromResources("day10.txt").readLines()
    println(input.map(::firstIllegalChar).map { points[it] ?: 0 }.sum())
}