package com.sad.aoc2021.day22

import com.sad.aoc2021.loadFromResources

fun Pair<Int, Int>.isOutside(): Boolean {
    return Math.min(this.first, this.second) < -50 || Math.min(this.first, this.second) > 50
}

data class Cuboid(val x: Pair<Int, Int>, val y: Pair<Int, Int>, val z: Pair<Int, Int>, val on: Boolean) {
    fun signedSize(): Long =
        (x.second.toLong() - x.first.toLong() + 1L) *
                (y.second.toLong() - y.first.toLong() + 1) * (z.second.toLong() - z.first.toLong() + 1) * (if (on) 1 else -1)

    fun isOutside(): Boolean {
        return x.isOutside() || y.isOutside() || z.isOutside()
    }
}

private fun parse(s: String): Cuboid {
    val (onoff, coords) = s.split(" ")
    val parsedCoords = coords.split(",").map { it.split("=")[1].split("..").map { s -> s.toInt() } }
    return Cuboid(
        Pair(parsedCoords[0][0], parsedCoords[0][1]),
        Pair(parsedCoords[1][0], parsedCoords[1][1]),
        Pair(parsedCoords[2][0], parsedCoords[2][1]), onoff == "on"
    )
}

private fun intersectCoords(coord1: Pair<Int, Int>, coord2: Pair<Int, Int>): Pair<Int, Int>? {
    val from = Math.max(coord1.first, coord2.first)
    val to = Math.min(coord1.second, coord2.second)
    if (from <= to) {
        return Pair(from, to)
    } else {
        return null
    }
}

private fun intersection(cuboid1: Cuboid, cuboid2: Cuboid, on: Boolean): Cuboid? {
    val intersectX = intersectCoords(cuboid1.x, cuboid2.x)
    val intersectY = intersectCoords(cuboid1.y, cuboid2.y)
    val intersectZ = intersectCoords(cuboid1.z, cuboid2.z)
    if (intersectX != null && intersectY != null && intersectZ != null) {
        return Cuboid(intersectX, intersectY, intersectZ, on)
    } else {
        return null
    }
}

private fun solve(cuboids: List<Cuboid>, ignoreOutside: Boolean): Long {
    val result = mutableListOf<Cuboid>()
    result.add(cuboids[0])
    for (cuboid in cuboids.drop(1)) {
        if (ignoreOutside && cuboid.isOutside()) {
            continue
        }
//        println("Processing ${cuboid}")
        val toAdd = mutableListOf<Cuboid>()
        for (existingCuboid in result) {
            val intersection = intersection(cuboid, existingCuboid, !existingCuboid.on)
            if (intersection != null) {
                toAdd.add(intersection)
            }
        }
        if (cuboid.on) {
            toAdd.add(cuboid)
        }
        result.addAll(toAdd)
//        println("Result: ${result}")
    }
//    println(result)
    return result.map { it.signedSize() }.sum()
}

fun main() {
    val input = loadFromResources("day22.txt").readLines().map(::parse)
    println(solve(input, true))

}