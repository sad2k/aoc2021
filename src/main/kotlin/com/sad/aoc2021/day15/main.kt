package com.sad.aoc2021.day15

import com.sad.aoc2021.loadFromResources
import java.util.*

private fun traverse(map: List<List<Int>>): List<List<Int>> {
    val risk = MutableList(map.size) { MutableList(map[0].size) { 0 } }
    for (i in map.indices) {
        val row = map[i]
        for (j in row.indices) {
            if (i == 0 && j == 0) {
                risk[i][j] = 0
            } else {
                var bestPathSoFar = Int.MAX_VALUE
                if (i > 0 && risk[i - 1][j] < bestPathSoFar) {
                    bestPathSoFar = risk[i - 1][j]
                }
                if (j > 0 && risk[i][j - 1] < bestPathSoFar) {
                    bestPathSoFar = risk[i][j - 1]
                }
                risk[i][j] = map[i][j] + bestPathSoFar
            }
        }
    }
    return risk
}

private fun extend(input: List<List<Int>>): List<List<Int>> {
    var res = MutableList(input.size * 5) { MutableList(input[0].size * 5) { 0 } }
    for (i in 0 until 5) {
        for (j in 0 until 5) {
            for (ii in input.indices) {
                val origRow = input[ii]
                for (jj in origRow.indices) {
                    var newVal = origRow[jj] + (i + j)
                    if (newVal > 9) {
                        newVal = newVal - 9
                    }
                    res[ii + i * input.size][jj + j * input[0].size] = newVal
                }
            }
        }
    }
    return res
}

//data class CoordWithDistance(val row: Int, val col: Int, val distance: Int)

class CoordWithDistance(val row: Int, val col: Int, val distance: Long) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoordWithDistance

        if (row != other.row) return false
        if (col != other.col) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }
}

private fun traverseDijkstra(input: List<List<Int>>): Long {
    fun updateNeighbor(
        current: CoordWithDistance, newRow: Int, newCol: Int, currentRisk: MutableList<MutableList<Long>>,
        pq: PriorityQueue<CoordWithDistance>
    ) {
        val newNeighbour = current.distance + input[newRow][newCol]
        val currentNeighbour = currentRisk[newRow][newCol]
        if (newNeighbour < currentNeighbour) {
            currentRisk[newRow][newCol] = newNeighbour
            val upCoordWithDistance = CoordWithDistance(newRow, newCol, newNeighbour)
            pq.remove(upCoordWithDistance)
            pq.add(upCoordWithDistance)
        }
    }

    val risk = MutableList(input.size) { MutableList(input[0].size) { Long.MAX_VALUE } }
    risk[0][0] = 0
    val pq = PriorityQueue<CoordWithDistance>(compareBy { it.distance })
    pq.add(CoordWithDistance(0, 0, 0))
    while (!pq.isEmpty()) {
        val best = pq.remove()
        if (best.row == input.size - 1 && best.col == input[0].size - 1) {
            return best.distance
        }
        // step up
        if (best.row > 0) {
            updateNeighbor(best, best.row - 1, best.col, risk, pq)
        }
        // step down
        if (best.row < input.size - 1) {
            updateNeighbor(best, best.row + 1, best.col, risk, pq)
        }
        // step left
        if (best.col > 0) {
            updateNeighbor(best, best.row, best.col - 1, risk, pq)
        }
        // step right
        if (best.col < input[0].size - 1) {
            updateNeighbor(best, best.row, best.col + 1, risk, pq)
        }
    }
    throw IllegalArgumentException("didn't reach the exit?")
}

fun main() {
    val input = loadFromResources("day15.txt").readLines().map {
        it.map { ch ->
            ch.digitToInt()
        }
    }

    // part 1
    println(traverseDijkstra(input))

    // part 2
    val extendedInput = extend(input)
    println(traverseDijkstra(extendedInput))

}