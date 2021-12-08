package com.sad.aoc2021.day8

import com.sad.aoc2021.loadFromResources

fun main() {
    val input = loadFromResources("day8.txt").readLines()

    // part 1
    val uniqueLengths = listOf(2, 3, 4, 7)
    println(input.flatMap { it -> it.split("|")[1].trim().split(" ").filter { s -> s.length in uniqueLengths} }.count())
}