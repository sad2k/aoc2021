package com.sad.aoc2021.day8

import com.sad.aoc2021.loadFromResources

private fun String.sorted(): String {
    return this.toList().sorted().joinToString(separator = "")
}

fun decode(s: String): Long {
    val (input, output) = s.split("|").map(String::trim)
    val map = mutableMapOf<Int, String>()
    val inputStrings = input.split(" ")
    inputStrings.forEach { str ->
        var digit = -1
        if (str.length == 2) {
            digit = 1
        } else if (str.length == 3) {
            digit = 7
        } else if (str.length == 4) {
            digit = 4
        } else if (str.length == 7) {
            digit = 8
        }
        if (digit > 0) {
            map.put(digit, str.sorted())
        }
    }
    // top line
    val top = (map[7]!!.toSet() - map[1]!!.toSet()).first()
    // top left
    val topLeft = inputStrings.filter { it.length == 5 }.joinToString(separator = "").groupingBy { it }.eachCount().filter {
        it.value == 1
    }.map { it.key }.filter { map[4]!!.contains(it) }.toList()[0]
    // middle
    val middle = (map[4]!!.toSet() - map[1]!!.toSet() - setOf(topLeft)).first()
    // bottom left
    val bottomLeft = inputStrings.filter { it.length == 5 }.joinToString(separator = "").groupingBy { it }.eachCount().filter {
        it.value == 1
    }.map { it.key }.filter { !(map[4]!!.contains(it)) }.toList()[0]
    // bottom
    val bottom = (inputStrings.filter { it.length == 5 }.map(String::toSet).reduce{ a, b -> a intersect b }.toSet() - setOf(top) - setOf(middle)).first()
    // top right
    val topRight = (inputStrings.filter { it.length == 6 }.joinToString(separator = "").groupingBy { it }.eachCount().filter {
        it.value == 2
    }.map { it.key }.toSet() - setOf(middle) - setOf(bottomLeft)).first()
    // bottom right
    val bottomRight = (map[1]!!.toSet() - setOf(topRight)).first()
    // fill in the remaining numbers
    val decodeMap = mutableMapOf<String, Int>()
    map.forEach{ (value, str) -> decodeMap[str] = value }
    decodeMap["${top}${topLeft}${topRight}${bottomLeft}${bottomRight}${bottom}".sorted()] = 0
    decodeMap["${top}${topRight}${middle}${bottomLeft}${bottom}".sorted()] = 2
    decodeMap["${top}${topRight}${middle}${bottomRight}${bottom}".sorted()] = 3
    decodeMap["${top}${topLeft}${middle}${bottomRight}${bottom}".sorted()] = 5
    decodeMap["${top}${topLeft}${middle}${bottomLeft}${bottomRight}${bottom}".sorted()] = 6
    decodeMap["${top}${topLeft}${topRight}${middle}${bottomRight}${bottom}".sorted()] = 9
    // decode
    return output.split(" ").map { decodeMap[it.sorted()] }.joinToString(separator = "").toLong()
}

fun main() {
    val input = loadFromResources("day8.txt").readLines()

    // part 1
    val uniqueLengths = listOf(2, 3, 4, 7)
    println(input.flatMap { it -> it.split("|")[1].trim().split(" ").filter { s -> s.length in uniqueLengths} }.count())

    // part 2
    println(input.map(::decode).sum())

}