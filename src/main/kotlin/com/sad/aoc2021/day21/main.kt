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

fun play(state: GameState, player: Int): GameState {
    fun roll(prevRoll: Int): Int {
        var roll = prevRoll + 1
        if (roll == 101) {
            roll = 1
        }
        println("rolled ${roll}")
        return roll
    }

    fun updatePos(pos: Int, roll: Int): Int {
        return ((pos-1 + roll) % 10) + 1
    }

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

fun main() {
    val input = loadFromResources("day21.txt").readLines().map { it.split(": ")[1].toInt() }
    println(input)
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
}