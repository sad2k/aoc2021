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

fun toPairs(s: String): Map<String, Long> {
    return s.windowed(2, 1).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
}

fun transformOptimized(pairs: Map<String, Long>, rules: Map<String, String>): Map<String, Long> {
    val res = mutableMapOf<String, Long>()
    pairs.forEach { (k, v) ->
        val rule = rules[k] ?: throw IllegalArgumentException("no rule for ${k}")
        val newRule1 = k[0] + rule
        val newRule2 = rule + k[1]
        res.put(newRule1, res.getOrDefault(newRule1, 0) + v)
        res.put(newRule2, res.getOrDefault(newRule2, 0) + v)
    }
    return res
}

fun transformOptimizedTimes(pairs: Map<String, Long>, rules: Map<String, String>, times: Int): Map<String, Long> {
    var res = pairs
    for (i in 1..times) {
        res = transformOptimized(res, rules)
    }
    return res
}

fun countCharsInPairs(pairs: Map<String, Long>, originalTemplate: String): Map<Char, Long> {
    val res = pairs.map { (k, v) -> Pair(k[0], v) }.groupingBy { it.first }.fold(0L) { acc, el ->
        acc + el.second
    }.toMutableMap()
    val lastOrigTempChar = originalTemplate[originalTemplate.length - 1]
    res[lastOrigTempChar] = res.getOrDefault(lastOrigTempChar, 0) + 1
    return res
}

fun main() {
    fun solveFor(template: String, rules: Map<String, String>, times: Int) {
        val finalPairs = transformOptimizedTimes(toPairs(template), rules, times)
        val freq = countCharsInPairs(finalPairs, template)
        val mostFreq = freq.map { (k, v) -> Pair(k, v) }.maxWithOrNull(compareBy({ it.second }))!!.second
        val leastFreq = freq.map { (k, v) -> Pair(k, v) }.minWithOrNull(compareBy({ it.second }))!!.second
        println(mostFreq - leastFreq)
    }

    val input = loadFromResources("day14.txt").readLines().splitWhen { it.trim().isEmpty() }
    val template = input[0][0]
    val rules = createRuleMap(input[1])

    // part 1
    solveFor(template, rules, 10)

    // part 2
    solveFor(template, rules, 40)
}


