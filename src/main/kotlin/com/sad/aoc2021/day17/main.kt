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
    var iteration = 0
    while (!finished) {
        path.add(coord)

        if (coord.x >= xTarget.first && coord.x <= xTarget.second && coord.y >= yTarget.first && coord.y <= yTarget.second) {
            inTarget = true
            break
        }

        // is this the right condition? seems to work
        if (coord.x > xTarget.second || coord.y < yTarget.first) {
            inTarget = false
            break
        }

        coord = Coord(coord.x + currentXVelocity, coord.y + currentYVelocity)
        currentXVelocity += if (currentXVelocity > 0) {
            -1
        } else if (currentXVelocity < 0) {
            1
        } else {
            0
        }
        currentYVelocity -= 1
        iteration++
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
    var values = 0
    for(xv in 0..315) {
        for(yv in -500..500) {
            val (path, inTarget) = simulate(xv, yv, xTarget, yTarget)
            if (inTarget) {
                values++
                val height = path.maxOf { it.y }
                if (height > maxHeight) {
                    maxHeight = height
                }
            }
        }
    }
    println(maxHeight)
    println(values)
}