package com.sad.aoc2021.day16

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.readFirstLine
import java.lang.IllegalStateException
import java.lang.StringBuilder

fun parse(str: List<Char>, versionCallback: (Int) -> Unit): Pair<List<Char>, Long> {
    var current = str

    // version
    val version = current.take(3).joinToString(separator = "").toInt(2)
    current = current.drop(3)
    versionCallback(version)

    // type id
    val typeId = current.take(3).joinToString(separator = "").toInt(2)
    current = current.drop(3)

    if (typeId == 4) {
        // literal value
        var lastBits = false
        var bitsBuilder = StringBuilder()
        while (!lastBits) {
            val lastOrNot = current.take(1)[0]
            current = current.drop(1)
            val bits = current.take(4).joinToString(separator = "")
            current = current.drop(4)
            bitsBuilder.append(bits)
            lastBits = lastOrNot == '0'
        }
        val res = bitsBuilder.toString().toLong(2)
        return Pair(current, res)
    } else {
        // operator
        val lengthTypeId = current.take(1)[0]
        current = current.drop(1)
        val results = mutableListOf<Long>()
        if (lengthTypeId == '0') {
            // length
            val length = current.take(15).joinToString(separator = "").toInt(2)
            current = current.drop(15)
            val initialSize = current.size
            var consumed = 0
            while (consumed < length) {
                val pair = parse(current, versionCallback)
                current = pair.first
                results.add(pair.second)
                consumed = initialSize - current.size
            }
            if (consumed > length) {
                throw IllegalStateException("overconsumed? ${consumed} instead of ${length}")
            }
        } else {
            // number of sub packets
            val numberOfSubPackets = current.take(11).joinToString(separator = "").toInt(2)
            current = current.drop(11)
            for (i in 1..numberOfSubPackets) {
                val pair = parse(current, versionCallback)
                current = pair.first
                results.add(pair.second)
            }
        }

        return Pair(current, when(typeId) {
            0 -> if (results.size != 1) results.sum() else results[0]
            1 -> if (results.size != 1) results.reduce{ v1, v2 -> v1*v2 } else results[0]
            2 -> results.minOrNull()!!
            3 -> results.maxOrNull()!!
            5 -> if (results[0] > results[1]) 1 else 0
            6 -> if (results[0] < results[1]) 1 else 0
            7 -> if (results[0] == results[1]) 1 else 0
            else -> throw IllegalArgumentException("unsupported typeId: ${typeId}")
        })
    }
}

fun convertToBinary(str: String): List<Char> {
    return str.map {
        when (it) {
            '0' -> "0000"
            '1' -> "0001"
            '2' -> "0010"
            '3' -> "0011"
            '4' -> "0100"
            '5' -> "0101"
            '6' -> "0110"
            '7' -> "0111"
            '8' -> "1000"
            '9' -> "1001"
            'A' -> "1010"
            'B' -> "1011"
            'C' -> "1100"
            'D' -> "1101"
            'E' -> "1110"
            'F' -> "1111"
            else -> throw IllegalArgumentException("unsupported character: ${it}")
        }
    }.joinToString(separator = "").toCharArray().toList()
}

fun main() {
    val input = loadFromResources("day16.txt").readFirstLine()
    val converted = convertToBinary(input)
    var versionSum = 0
    println(parse(converted, {
        versionSum += it
    }))
    println("For part 1, version sum: ${versionSum}")
}