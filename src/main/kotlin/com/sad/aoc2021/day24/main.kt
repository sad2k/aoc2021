package com.sad.aoc2021.day24

import kotlin.system.measureTimeMillis

class Solver(val isMax: Boolean) {

    val stepParams = listOf(
        listOf(1, 12, 4),
        listOf(1, 15, 11),
        listOf(1, 11, 7),
        listOf(26, -14, 2),
        listOf(1, 12, 11),
        listOf(26, -10, 13),
        listOf(1, 11, 9),
        listOf(1, 13, 12),
        listOf(26, -7, 6),
        listOf(1, 10, 2),
        listOf(26, -2, 11),
        listOf(26, -1, 12),
        listOf(26, -4, 3),
        listOf(26, -12, 13)
    )

    private fun evaluateStep(step: Int, w: Long, inZ: Long): Long {
        val params = stepParams[step]
        var z = inZ
        val x = z % 26 + params[1]
        z /= params[0]
        if (x != w) {
            z *= 26
            z += w + params[2]
        }
        return z
    }

    val cache = List(14) { mutableMapOf<Long, CachedResult>() }

    data class CachedResult(val str: String?)

    fun find(step: Int, z: Long): String? {
        if (step == stepParams.size) {
            if (z == 0L) {
                return ""
            } else {
                return null
            }
        }

        val stepCache = cache[step]
        var cachedResult = stepCache[z]
        val res: String?
        if (cachedResult == null) {
            res = find0(step, z)
            cachedResult = CachedResult(res)
            stepCache[z] = cachedResult
        } else {
            res = cachedResult.str
        }
        return res
    }

    private fun find0(step: Int, z: Long): String? {
        val loop: IntProgression = if (isMax) (9 downTo 1) else (1..9)
        for (digit in loop) {
            val newZ = evaluateStep(step, digit.toLong(), z)
            val recursiveBest = find(step + 1, newZ)
            if (recursiveBest != null) {
                return digit.toString() + recursiveBest
            }
        }

        return null
    }
}

fun main() {
    // part 1
    val time1 = measureTimeMillis {
        println(Solver(true).find(0, 0))
    }
    println("Done in ${time1} ms")

    // part 2
    val time2 = measureTimeMillis {
        println(Solver(false).find(0, 0))
    }
    println("Done in ${time2} ms")
}