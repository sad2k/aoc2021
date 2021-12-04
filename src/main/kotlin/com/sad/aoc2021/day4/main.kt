package com.sad.aoc2021.day4

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.splitWhen
import java.lang.IllegalStateException

interface MarkingStatus

object NotFullyMarked : MarkingStatus

data class RowMarked(val row: Int) : MarkingStatus

data class ColumnMarked(val col: Int) : MarkingStatus

data class Result(val number: Int, val grid: List<List<Int>>, val gridState: List<MutableList<Boolean>>, val markingStatus: MarkingStatus)

fun findFirstWinningGrid(numbers: List<Int>, grids: List<List<List<Int>>>): Result {
    val states: List<List<MutableList<Boolean>>> = grids.map { it.map { it.map { false }.toMutableList() } }
    for (num in numbers) {
        for (i in grids.indices) {
            val grid = grids[i]
            val gridState = states[i]
            val markingStatus = markNumber(grid, gridState, num)
            if (markingStatus != NotFullyMarked) {
                return Result(num, grid, gridState, markingStatus)
            }
        }
    }
    throw IllegalStateException("no winner detected")
}

fun findLastWinningGrid(numbers: List<Int>, grids: List<List<List<Int>>>): Result {
    val states: List<List<MutableList<Boolean>>> = grids.map { it.map { it.map { false }.toMutableList() } }
    val pendingGrids = grids.indices.toMutableSet()
    for (num in numbers) {
        for (i in grids.indices) {
            if (i in pendingGrids) {
                val grid = grids[i]
                val gridState = states[i]
                val markingStatus = markNumber(grid, gridState, num)
                if (markingStatus != NotFullyMarked) {
                    if (pendingGrids.size == 1) {
                        return Result(num, grid, gridState, markingStatus)
                    } else {
                        pendingGrids.remove(i)
                    }
                }
            }
        }
    }
    throw IllegalStateException("no winner detected")
}

fun markNumber(grid: List<List<Int>>, gridState: List<MutableList<Boolean>>, number: Int): MarkingStatus {
    var marked = false
    for (i in grid.indices) {
        val row = grid[i]
        for (j in row.indices) {
            if (row[j] == number) {
                gridState[i][j] = true
                marked = true
            }
        }
    }
    if (marked) {
        return findMarkingStatus(gridState)
    }
    return NotFullyMarked
}

fun findMarkingStatus(gridState: List<MutableList<Boolean>>): MarkingStatus {
    // check rows
    for (i in gridState.indices) {
        val row = gridState[i]
        if (row.all { it }) {
            return RowMarked(i)
        }
    }
    // check columns
    for (i in gridState[0].indices) { // doesnt matter though as they are 5x5
        var allTrue = true
        for (j in gridState.indices) {
            if (!gridState[j][i]) {
                allTrue = false
                continue
            }
        }
        if (allTrue) {
            return ColumnMarked(i)
        }
    }
    return NotFullyMarked
}

private fun calculateUnmarkedSum(grid: List<List<Int>>, gridState: List<MutableList<Boolean>>): Int {
    var sum = 0
    for(i in grid.indices) {
        val row = grid[i]
        for(j in row.indices) {
            if (!gridState[i][j]) {
                sum += grid[i][j]
            }
        }
    }
    return sum
}

fun main() {
    val input = loadFromResources("day4.txt").readLines()
    val split = input.splitWhen { it.trim().isEmpty() }
    val numbers = split[0][0].split(",").map(String::toInt)
    val grids = split.slice(1 until split.size)
        .map { it.map { str -> str.split("\\s+".toRegex()).filter(String::isNotEmpty).map(String::toInt) } }

    // part 1
    val res = findFirstWinningGrid(numbers, grids)
    val unmarkedSum = calculateUnmarkedSum(res.grid, res.gridState)
    println(res.number * unmarkedSum)

    // part 2
    val res2 = findLastWinningGrid(numbers, grids)
    val unmarkedSum2 = calculateUnmarkedSum(res2.grid, res2.gridState)
    println(res2.number * unmarkedSum2)
}



