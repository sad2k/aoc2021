package com.sad.aoc2021.day19

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.splitWhen

data class Coord(val x: Int, val y: Int, val z: Int) {
    operator fun minus(that: Coord): Coord {
        return Coord(x - that.x, y - that.y, z - that.z)
    }

    operator fun plus(that: Coord): Coord {
        return Coord(x + that.x, y + that.y, z + that.z)
    }

    fun manhattanDistance(that: Coord): Int {
        return Math.abs(x - that.x) + Math.abs(y - that.y) + Math.abs(z - that.z)
    }
}

// https://imgur.com/a/g1JVC3F
val transformations = listOf<(Coord) -> Coord>(
    { c -> Coord(c.x, c.y, c.z) },
    { c -> Coord(c.x, c.z, -c.y) },
    { c -> Coord(c.x, -c.y, -c.z) },
    { c -> Coord(c.x, -c.z, c.y) },

    { c -> Coord(-c.x, c.y, c.z) },
    { c -> Coord(-c.x, c.z, c.y) },
    { c -> Coord(-c.x, c.y, -c.z) },
    { c -> Coord(-c.x, -c.z, -c.y) },

    { c -> Coord(c.y, c.z, c.x) },
    { c -> Coord(c.y, c.x, -c.z) },
    { c -> Coord(c.y, -c.z, -c.x) },
    { c -> Coord(c.y, -c.x, c.z) },

    { c -> Coord(-c.y, -c.z, c.x) },
    { c -> Coord(-c.y, c.x, c.z) },
    { c -> Coord(-c.y, c.z, -c.x) },
    { c -> Coord(-c.y, -c.x, -c.z) },

    { c -> Coord(c.z, c.x, c.y) },
    { c -> Coord(c.z, c.y, -c.x) },
    { c -> Coord(c.z, -c.x, -c.y) },
    { c -> Coord(c.z, -c.y, c.x) },

    { c -> Coord(-c.z, -c.x, c.y) },
    { c -> Coord(-c.z, c.y, c.x) },
    { c -> Coord(-c.z, c.x, -c.y) },
    { c -> Coord(-c.z, -c.y, -c.x) }
)

private fun parseCoord(s: String): Coord {
    val spl = s.split(",")
    return Coord(spl[0].toInt(), spl[1].toInt(), spl[2].toInt())
}

data class ScannerDetails(val delta: Coord, val beacons: List<Coord>, val transform: (Coord) -> Coord, val deltaTo: Int)

private fun findOverlapped(
    scanner1: List<Coord>,
    scanner2: List<Coord>
): Triple<Coord, MutableList<Coord>, (Coord) -> Coord>? {
    for (transform in transformations) {
//        println("---> transformation ${transform}")
        val scanner2trans = scanner2.map(transform)
        val map = mutableMapOf<Coord, MutableList<Coord>>()
        for (i in scanner1.indices) {
            for (j in scanner2trans.indices) {
                val delta = scanner1[i] - scanner2trans[j]
                val listForDelta = map.computeIfAbsent(delta, { mutableListOf() })
                listForDelta.add(scanner1[i])
            }
        }
        val overlap = map.filter { e -> e.value.size == 12 }
        if (overlap.size > 0) {
            if (overlap.size > 1) {
                throw IllegalStateException("more than one candidate - unexpected: ${overlap}")
            }
            val entry = overlap.entries.first()
            return Triple(entry.key, entry.value, transform)
        }
    }
    return null
}

fun resolveScanners(scanners: List<List<Coord>>): Map<Int, ScannerDetails> {
    val baseScanners = mutableListOf(0)
    val unresolvedScanners = (scanners.indices - baseScanners).toMutableList()
    val resolvedScanners = mutableMapOf<Int, ScannerDetails>()
    resolvedScanners[0] = ScannerDetails(Coord(0, 0, 0), emptyList(), { it }, 0)

    while (!baseScanners.isEmpty()) {
        val baseScanner = baseScanners.removeAt(0)
        val resolved = mutableListOf<Int>()
        for (i in unresolvedScanners) {
            val scanner1 = scanners[baseScanner]
            val overlap = findOverlapped(scanner1, scanners[i])
            if (overlap != null) {
                resolved.add(i)
                val details = ScannerDetails(overlap.first, overlap.second, overlap.third, baseScanner)
                resolvedScanners[i] = details
//                println("Adding scanner details ${i} -> ${details}")
                baseScanners.add(i)
            }
        }
        unresolvedScanners.removeAll(resolved)
    }

    return resolvedScanners
}

private fun findBeacons(scanners: List<List<Coord>>, details: Map<Int, ScannerDetails>): MutableSet<Coord> {
    val allBeacons = mutableSetOf<Coord>()
    for (i in 0 until details.size) {
        var beacons = scanners[i]
        var currentScanner = i

        while (currentScanner != 0) {
            val dets = details[currentScanner]!!
            beacons = beacons.map(dets.transform).map { it + dets.delta }
            currentScanner = dets.deltaTo
        }

        allBeacons.addAll(beacons)
    }

    return allBeacons
}

private fun findScannerLocation(idx: Int, details: Map<Int, ScannerDetails>): Coord {
    var currentScanner = idx
    var coord = Coord(0, 0, 0)
    while (currentScanner != 0) {
        val dets = details[currentScanner]!!
        coord = dets.transform(coord)
        coord = coord + dets.delta
        currentScanner = dets.deltaTo
    }
    return coord
}

fun main() {
    val input = loadFromResources("day19.txt").readLines().splitWhen { it.trim().isEmpty() }
        .map { it.drop(1).map { str -> parseCoord(str) } }

    // part 1
    val scannerDetails = resolveScanners(input)
    println(findBeacons(input, scannerDetails).size)

    // part 2
    var max = 0
    for (i in input.indices) {
        val loc = findScannerLocation(i, scannerDetails)
        for (j in i + 1 until input.size) {
            val loc2 = findScannerLocation(j, scannerDetails)
            val dist = loc.manhattanDistance(loc2)
            if (dist > max) {
                max = dist
            }
        }
    }
    println(max)
}


