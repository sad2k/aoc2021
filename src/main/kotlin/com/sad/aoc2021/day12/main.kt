package com.sad.aoc2021.day12

import com.sad.aoc2021.loadFromResources

fun buildGraph(input: List<String>): Map<String, List<String>> {
    val res = mutableMapOf<String, MutableList<String>>()
    input.map { it.split("-") }.forEach {
        val from = it[0]
        val to = it[1]
        res.computeIfAbsent(from) { mutableListOf() }.add(to)
        res.computeIfAbsent(to) { mutableListOf() }.add(from)
    }
    return res
}

fun findPaths(graph: Map<String, List<String>>, start: String, bypassing: Set<String>): List<List<String>> {
    if (start == "end") {
        return listOf(listOf("end"))
    } else {
        return graph[start]!!.filter {
            !(it in bypassing)
        }.flatMap {
            val newBypassing = if (start[0].isLowerCase()) {
                bypassing + setOf(start)
            } else {
                bypassing
            }
            findPaths(graph, it, newBypassing)
        }.map {
            listOf(start) + it
        }
    }
}

fun findPaths2(graph: Map<String, List<String>>, start: String, timesVisited: Map<String, Int>, allowedTwice: String): List<List<String>> {
    if (start == "end") {
        return listOf(listOf("end"))
    } else {
        return graph[start]!!.filter {
            val tv = timesVisited.getOrDefault(it, 0)
            tv == 0 || (tv == 1 && it == allowedTwice)
        }.flatMap {
            val newTimesVisited = if (start[0].isLowerCase()) {
                val oldtv = timesVisited.getOrDefault(start, 0)
                timesVisited + Pair(start, oldtv+1)
            } else {
                timesVisited
            }
            findPaths2(graph, it, newTimesVisited, allowedTwice)
        }.map {
            listOf(start) + it
        }
    }
}

fun main() {
    val input = loadFromResources("day12.txt").readLines()
    val graph = buildGraph(input)

    // part 1
    println(findPaths(graph, "start", emptySet()).size)

    // part 2
    var res = mutableSetOf<List<String>>()
    for(smallCave in graph.keys.filter { it != "start" && it != "end" && it[0].isLowerCase() }) {
        val paths = findPaths2(graph, "start", emptyMap(), smallCave)
        res.addAll(paths)
    }
    println(res.size)
}