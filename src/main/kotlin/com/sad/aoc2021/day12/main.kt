package com.sad.aoc2021.day12

import com.sad.aoc2021.loadFromResources

fun buildGraph(input: List<String>): Map<String, List<String>> {
    val res = mutableMapOf<String, MutableList<String>>()
    input.map { it.split("-") }.forEach {
        val from = it[0]
        val to = it[1]
        res.computeIfAbsent(from, { _ -> mutableListOf<String>() }).add(to)
        res.computeIfAbsent(to, { _ -> mutableListOf<String>() }).add(from)
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

fun main() {
    val input = loadFromResources("day12.txt").readLines()
    val graph = buildGraph(input)
    println(findPaths(graph, "start", emptySet()).size)
}