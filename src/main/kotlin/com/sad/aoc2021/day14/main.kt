package com.sad.aoc2021.day14

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.splitWhen
import java.lang.IllegalArgumentException

fun createRuleMap(list: List<String>): Map<String, String> {
    return list.associate {
        val spl = it.split(" -> ")
        Pair(spl[0], spl[1])
    }
}

fun transform(template: String, rules: Map<String, String>): String {
    val pairs = template.windowed(2, 1)
    val insertions = pairs.map { rules[it] ?: throw IllegalArgumentException("no rule for ${it}") }
    return (pairs zip insertions).map { (pair, insertion) ->
        pair[0] + insertion
    }.joinToString(separator = "") + pairs[pairs.size - 1][1]
}

fun transformTimes(template: String, rules: Map<String, String>, times: Int): String {
    var res = template
    for (i in 1..times) {
        res = transform(res, rules)
    }
    return res
}

fun countChars(s: String): Map<Char, Int> {
    return s.groupingBy { it }.eachCount()
}

fun main() {
    val input = loadFromResources("day14.txt").readLines().splitWhen { it.trim().isEmpty() }
    val template = input[0][0]
    val rules = createRuleMap(input[1])
    val freq = countChars(transformTimes(template, rules, 10))
    val mostFreq = freq.map { (k, v) -> Pair(k, v) }.maxWithOrNull(compareBy({ it.second }))!!.second
    val leastFreq = freq.map { (k, v) -> Pair(k, v) }.minWithOrNull(compareBy({ it.second }))!!.second
    println(mostFreq - leastFreq)
}


