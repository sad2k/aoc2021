package com.sad.aoc2021.day17

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.readFirstLine

data class Coord(val x: Int, val y: Int)

fun simulate(xVelocity: Int, yVelocity: Int, xTarget: Pair<Int, Int>, yTarget: Pair<Int, Int>):
        Pair<List<Coord>, Boolean> {
    val path = mutableListOf<Coord>()
    var inTarget = false
    var finished = false
    var coord = Coord(0, 0)
    var currentXVelocity = xVelocity
    var currentYVelocity = yVelocity
    while (!finished) {
        path.add(coord)

        if (coord.x >= xTarget.first && coord.x <= xTarget.second && coord.y >= yTarget.first && coord.y <= yTarget.second) {
            inTarget = true
            break
        }

        if (coord.x > xTarget.second) {
            inTarget = false
            break
        }

        val xAdj = if (currentXVelocity > 0) {
            -1
        } else if (currentYVelocity < 0) {
            1
        } else {
            0
        }
        coord = Coord(coord.x + currentXVelocity, coord.y + currentYVelocity)
        currentXVelocity += xAdj
        currentYVelocity -= 1
    }
    return Pair(path, inTarget)
}

fun main() {
    val input = loadFromResources("day17.txt").readFirstLine()
    val target = input.split(": ")[1].split(", ").map {
        it.split("=")[1].split("..").map(String::toInt)
    }
    val xTarget = Pair(target[0][0], target[0][1])
    val yTarget = Pair(target[1][0], target[1][1])

    // who cares - will use brute force
    var maxHeight = Integer.MIN_VALUE
    for(xv in -100..100) {
        for(yv in -100..100) {
            val (path, inTarget) = simulate(xv, yv, xTarget, yTarget)
            if (inTarget) {
                val height = path.maxOf { it.y }
                if (height > maxHeight) {
                    maxHeight = height
                }
            }
        }
    }

    println(maxHeight)
}