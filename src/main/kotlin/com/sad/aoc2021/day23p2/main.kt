package com.sad.aoc2021.day23p2

import com.sad.aoc2021.loadFromResources

abstract class Amphipod(val typeName: String, val energy: Long, val room: Int) {
    override fun toString(): String {
        return typeName
    }
}

class Amber : Amphipod("Amber", 1, 0)
class Bronze : Amphipod("Bronze", 10, 1)
class Copper : Amphipod("Copper", 100, 2)
class Desert : Amphipod("Desert", 1000, 3)

data class State(
    val hallway: List<Amphipod?>,
    val rooms: List<List<Amphipod?>>
) {
    fun moveFromRoomToHallway(room: Int, pos: Int, hallwayIdx: Int): State {
        val amphipod = rooms[room][pos]
        val newRooms = rooms.toMutableList().map { it.toMutableList() }
        newRooms[room][pos] = null
        val newHallway = hallway.toMutableList()
        newHallway[hallwayIdx] = amphipod
        return State(newHallway, newRooms)
    }

    fun moveFromHallwayToRoom(hallwayIdx: Int, room: Int, pos: Int): State {
        val amphipod = hallway[hallwayIdx]
        val newHallway = hallway.toMutableList()
        newHallway[hallwayIdx] = null
        val newRooms = rooms.toMutableList().map { it.toMutableList() }
        newRooms[room][pos] = amphipod
        return State(newHallway, newRooms)
    }

    fun isFinal(): Boolean {
        return rooms[0][0] is Amber && rooms[0][1] is Amber && rooms[0][2] is Amber && rooms[0][3] is Amber &&
                rooms[1][0] is Bronze && rooms[1][1] is Bronze && rooms[1][2] is Bronze && rooms[1][3] is Bronze &&
                rooms[2][0] is Copper && rooms[2][1] is Copper && rooms[2][2] is Copper && rooms[2][3] is Copper &&
                rooms[3][0] is Desert && rooms[3][1] is Desert && rooms[3][2] is Desert && rooms[3][3] is Desert
    }
}

private fun parse(ch: Char): Amphipod {
    return when (ch) {
        'A' -> Amber()
        'B' -> Bronze()
        'C' -> Copper()
        'D' -> Desert()
        else -> throw IllegalArgumentException("unsupported amphipod: ${ch}")
    }
}

val cache = mutableMapOf<State, Long?>()

val roomExitIdx = mapOf(0 to 2, 1 to 4, 2 to 6, 3 to 8)

private fun bestEnergy(state: State): Long? {
    if (!cache.containsKey(state)) {
        val res = bestEnergy0(state)
        cache.put(state, res)
        return res
    }
    return cache[state]
}

fun isRoomFinal(state: State, roomIdx: Int): Boolean {
    val room = state.rooms[roomIdx]
    if (room[0] == null && room[1] == null && room[2] == null && room[3] != null) {
        return room[3]!!.room == roomIdx
    } else if (room[0] == null && room[1] == null && room[2] != null && room[3] != null) {
        return room[2]!!.room == roomIdx && room[3]!!.room == roomIdx
    } else if (room[0] == null && room[1] != null && room[2] != null && room[3] != null) {
        return room[1]!!.room == roomIdx && room[2]!!.room == roomIdx && room[3]!!.room == roomIdx
    } else if (room[0] != null && room[1] != null && room[2] != null && room[3] != null) {
        return room[0]!!.room == roomIdx && room[1]!!.room == roomIdx && room[2]!!.room == roomIdx && room[3]!!.room == roomIdx
    } else {
        return false
    }
}

fun tryRoom(state: State, roomIdx: Int): Long? {
    var best: Long? = null
    if (isRoomFinal(state, roomIdx)) {
        return best
    }
    val room = state.rooms[roomIdx]
    val exit = roomExitIdx[roomIdx]!!
    var left: Int? = null
    for (i in (exit - 1) downTo 0) {
        if (state.hallway[i] == null) {
            left = i
        } else {
            break
        }
    }
    var right: Int? = null
    for (i in (exit + 1) until state.hallway.size) {
        if (state.hallway[i] == null) {
            right = i
        } else {
            break
        }
    }
    if (left != null || right != null) {
        left = left ?: exit + 1
        right = right ?: exit - 1
        if (room[0] != null) {
            // can move the top guy
            val amphipod = room[0]!!
            for (hallIdx in left..right) {
                if (!(hallIdx in roomExitIdx.values)) {
                    val newState = state.moveFromRoomToHallway(roomIdx, 0, hallIdx)
                    val moveEnergy = (1 + Math.abs(hallIdx - exit)) * amphipod.energy
                    val newBest = bestEnergy(newState)
                    if (newBest != null) {
                        val totalEnergy = moveEnergy + newBest
                        if (isBetterThan(totalEnergy, best)) {
                            best = totalEnergy
                        }
                    }
                }
            }
        } else if (room[0] == null && room[1] != null) {
            // can move the second guy
            val amphipod = room[1]!!
            for (hallIdx in left..right) {
                if (!(hallIdx in roomExitIdx.values)) {
                    val newState = state.moveFromRoomToHallway(roomIdx, 1, hallIdx)
                    val moveEnergy = (2 + Math.abs(hallIdx - exit)) * amphipod.energy
                    val newBest = bestEnergy(newState)
                    if (newBest != null) {
                        val totalEnergy = moveEnergy + newBest
                        if (isBetterThan(totalEnergy, best)) {
                            best = totalEnergy
                        }
                    }
                }
            }
        } else if (room[0] == null && room[1] == null && room[2] != null) {
            // can move the third guy
            val amphipod = room[2]!!
            for (hallIdx in left..right) {
                if (!(hallIdx in roomExitIdx.values)) {
                    val newState = state.moveFromRoomToHallway(roomIdx, 2, hallIdx)
                    val moveEnergy = (3 + Math.abs(hallIdx - exit)) * amphipod.energy
                    val newBest = bestEnergy(newState)
                    if (newBest != null) {
                        val totalEnergy = moveEnergy + newBest
                        if (isBetterThan(totalEnergy, best)) {
                            best = totalEnergy
                        }
                    }
                }
            }
        } else if (room[0] == null && room[1] == null && room[2] == null && room[3] != null) {
            // can move the bottom guy
            val amphipod = room[3]!!
            for (hallIdx in left..right) {
                if (!(hallIdx in roomExitIdx.values)) {
                    val newState = state.moveFromRoomToHallway(roomIdx, 3, hallIdx)
                    val moveEnergy = (4 + Math.abs(hallIdx - exit)) * amphipod.energy
                    val newBest = bestEnergy(newState)
                    if (newBest != null) {
                        val totalEnergy = moveEnergy + newBest
                        if (isBetterThan(totalEnergy, best)) {
                            best = totalEnergy
                        }
                    }
                }
            }
        }
    }
    return best
}

fun tryHallway(state: State, hallwayIdx: Int): Long? {
    var best: Long? = null
    val amphipod = state.hallway[hallwayIdx]!!
    val roomIdx = amphipod.room
    val roomExit = roomExitIdx[roomIdx]!!
    // can we reach the room?
    var pathClear = true
    if (roomExit < hallwayIdx) {
        // need to go left
        for (i in roomExit until hallwayIdx) {
            if (state.hallway[i] != null) {
                pathClear = false
                break
            }
        }
    } else {
        // need to go right
        for (i in (hallwayIdx + 1)..roomExit) {
            if (state.hallway[i] != null) {
                pathClear = false
                break
            }
        }
    }
    if (!pathClear) {
        return null
    }
    val room = state.rooms[roomIdx]
    if (room[0] == null && room[1] != null && room[1]!!.javaClass == amphipod.javaClass
        && room[2] != null && room[2]!!.javaClass == amphipod.javaClass
        && room[3] != null && room[3]!!.javaClass == amphipod.javaClass
    ) {
        // can move to the top of the room
        val newState = state.moveFromHallwayToRoom(hallwayIdx, roomIdx, 0)
        val moveEnergy = (Math.abs(hallwayIdx - roomExit) + 1) * amphipod.energy
        val newBest = bestEnergy(newState)
        if (newBest != null) {
            val totalEnergy = moveEnergy + newBest
            if (isBetterThan(totalEnergy, best)) {
                best = totalEnergy
            }
        }
    } else if (room[0] == null && room[1] == null
        && room[2] != null && room[2]!!.javaClass == amphipod.javaClass
        && room[3] != null && room[3]!!.javaClass == amphipod.javaClass
    ) {
        // can move to the second row
        val newState = state.moveFromHallwayToRoom(hallwayIdx, roomIdx, 1)
        val moveEnergy = (Math.abs(hallwayIdx - roomExit) + 2) * amphipod.energy
        val newBest = bestEnergy(newState)
        if (newBest != null) {
            val totalEnergy = moveEnergy + newBest
            if (isBetterThan(totalEnergy, best)) {
                best = totalEnergy
            }
        }
    } else if (room[0] == null && room[1] == null
        && room[2] == null
        && room[3] != null && room[3]!!.javaClass == amphipod.javaClass
    ) {
        // can move to the third row
        val newState = state.moveFromHallwayToRoom(hallwayIdx, roomIdx, 2)
        val moveEnergy = (Math.abs(hallwayIdx - roomExit) + 3) * amphipod.energy
        val newBest = bestEnergy(newState)
        if (newBest != null) {
            val totalEnergy = moveEnergy + newBest
            if (isBetterThan(totalEnergy, best)) {
                best = totalEnergy
            }
        }
    } else if (room[0] == null && room[1] == null
        && room[2] == null
        && room[3] == null
    ) {
        // can move to the bottom row
        val newState = state.moveFromHallwayToRoom(hallwayIdx, roomIdx, 3)
        val moveEnergy = (Math.abs(hallwayIdx - roomExit) + 4) * amphipod.energy
        val newBest = bestEnergy(newState)
        if (newBest != null) {
            val totalEnergy = moveEnergy + newBest
            if (isBetterThan(totalEnergy, best)) {
                best = totalEnergy
            }
        }
    }
    return best
}

private fun isBetterThan(newRes: Long?, currentRes: Long?): Boolean {
    if (newRes == null) {
        return false
    } else {
        if (currentRes == null) {
            return true
        } else {
            return newRes < currentRes
        }
    }
}

private fun bestEnergy0(state: State): Long? {
//    println("Calculating for ${state}")
    if (state.isFinal()) {
//        println("Final state: ${state}")
        return 0
    }
    // from rooms
    var best: Long? = null
    for (roomIdx in state.rooms.indices) {
        val roomRes = tryRoom(state, roomIdx)
        if (isBetterThan(roomRes, best)) {
            best = roomRes
        }
    }
    // from hallway
    for (hallwayIdx in state.hallway.indices) {
        if (state.hallway[hallwayIdx] != null) {
            val hallwayRes = tryHallway(state, hallwayIdx)
            if (isBetterThan(hallwayRes, best)) {
                best = hallwayRes
            }
        }
    }
    return best
}

fun main() {
    var lines = loadFromResources("day23.txt").readLines().drop(2).take(2).map { it.replace("#", "").replace(" ", "") }
    lines = listOf(lines[0], "DCBA", "DBAC", lines[1])
    val input =
        lines.map {
            it.map(::parse)
        }
    val initialState = State(
        List(11) { null }, listOf(
            listOf(input[0][0], input[1][0], input[2][0], input[3][0]),
            listOf(input[0][1], input[1][1], input[2][1], input[3][1]),
            listOf(input[0][2], input[1][2], input[2][2], input[3][2]),
            listOf(input[0][3], input[1][3], input[2][3], input[3][3])
        )
    )
    println(bestEnergy(initialState))
}