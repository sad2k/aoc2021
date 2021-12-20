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

fun findBeacons(scanners: List<List<Coord>>): Int {
    val baseScanners = mutableListOf(0)
    var unresolvedScanners = (scanners.indices - baseScanners).toMutableList()
    var resolvedScanners = mutableMapOf<Int, ScannerDetails>()
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

    var allBeacons = mutableSetOf<Coord>()
    for (i in scanners.indices) {

        var beacons = scanners[i]
        var currentScanner = i

        while (currentScanner != 0) {
            val dets = resolvedScanners[currentScanner]!!
            beacons = beacons.map(dets.transform).map { it + dets.delta }
            currentScanner = dets.deltaTo
        }

        allBeacons.addAll(beacons)
    }

    return allBeacons.size
}


fun main() {
    val input = loadFromResources("day19.txt").readLines().splitWhen { it.trim().isEmpty() }
        .map { it.drop(1).map { str -> parseCoord(str) } }
//    println(input)
//    println(findOverlapped(input[0], input[1]))
    println(findBeacons(input))
}