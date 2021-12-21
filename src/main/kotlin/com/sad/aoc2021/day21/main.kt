package com.sad.aoc2021.day21

import com.sad.aoc2021.loadFromResources

data class GameState(
    val p1pos: Int,
    val p2pos: Int,
    val p1score: Int,
    val p2score: Int,
    val rolls: Int,
    val lastRoll: Int
) {
    fun isWon(): Boolean = p1score >= 1000 || p2score >= 1000
}

data class GameSummary(val p1wins: Long, val p2wins: Long) {
    operator fun plus(that: GameSummary) = GameSummary(p1wins + that.p1wins, p2wins + that.p2wins)
}

fun roll(prevRoll: Int): Int {
    var roll = prevRoll + 1
    if (roll == 101) {
        roll = 1
    }
    return roll
}

fun updatePos(pos: Int, roll: Int): Int {
    return ((pos - 1 + roll) % 10) + 1
}

fun play(state: GameState, player: Int): GameState {
    val roll1 = roll(state.lastRoll)
    val roll2 = roll(roll1)
    val roll3 = roll(roll2)
    val roll = roll1 + roll2 + roll3
    val pos: Int
    val score: Int
    if (player == 1) {
        pos = updatePos(state.p1pos, roll)
        score = state.p1score + pos
        return GameState(pos, state.p2pos, score, state.p2score, state.rolls + 3, roll3)
    } else {
        pos = updatePos(state.p2pos, roll)
        score = state.p2score + pos
        return GameState(state.p1pos, pos, state.p1score, score, state.rolls + 3, roll3)
    }
}

data class DiracGameState(
    val p1pos: Int,
    val p2pos: Int,
    val p1score: Int,
    val p2score: Int
) {
    fun isWon(): Boolean = p1score >= 21 || p2score >= 21
}

val diracRolls = generateDiracRolls()

fun generateDiracRolls(): List<Int> {
    val rolls = mutableListOf<Int>()
    for (i in 1..3) {
        for (j in 1..3) {
            for (k in 1..3) {
                rolls.add(i + j + k)
            }
        }
    }
    return rolls
}

val cache = mutableMapOf<Pair<DiracGameState, Int>, GameSummary>()

fun playDiracCached(state: DiracGameState, player: Int): GameSummary {
    val key = Pair(state, player)
    var gs = cache[key]
    if (gs == null) {
        gs = playDirac(state, player)
        cache.put(key, gs)
    }
    return gs
}

fun playDirac(state: DiracGameState, player: Int): GameSummary {
    fun playPlayer1(roll: Int): GameSummary {
        val pos = updatePos(state.p1pos, roll)
        val newState = DiracGameState(pos, state.p2pos, state.p1score + pos, state.p2score)
        val summary: GameSummary
        if (newState.isWon()) {
            summary = GameSummary(1, 0)
        } else {
            summary = playDiracCached(newState, 2)
        }
        return summary
    }

    fun playPlayer2(roll: Int): GameSummary {
        val pos = updatePos(state.p2pos, roll)
        val newState = DiracGameState(state.p1pos, pos, state.p1score, state.p2score + pos)
        val summary: GameSummary
        if (newState.isWon()) {
            summary = GameSummary(0, 1)
        } else {
            summary = playDiracCached(newState, 1)
        }
        return summary
    }

    if (player == 1) {
        return diracRolls.map(::playPlayer1).reduce { gs1, gs2 -> gs1 + gs2 }
    } else {
        return diracRolls.map(::playPlayer2).reduce { gs1, gs2 -> gs1 + gs2 }
    }
}

fun main() {
    val input = loadFromResources("day21.txt").readLines().map { it.split(": ")[1].toInt() }

    // part 1
    var state = GameState(input[0], input[1], 0, 0, 0, 0)
    while (true) {
        state = play(state, 1)
        if (state.isWon()) {
            println("won by p1: ${state.p2score * state.rolls}")
            break
        }
        state = play(state, 2)
        if (state.isWon()) {
            println("won by p2: ${state.p1score * state.rolls}")
            break
        }
    }

    // part 2
    val diracState = DiracGameState(input[0], input[1], 0, 0)
    println(playDirac(diracState, 1))

}