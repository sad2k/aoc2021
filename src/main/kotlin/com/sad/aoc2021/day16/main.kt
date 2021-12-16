package com.sad.aoc2021.day16

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.readFirstLine
import java.lang.IllegalStateException
import java.lang.StringBuilder

fun parse(str: List<Char>, versionCallback: (Int) -> Unit): List<Char> {
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
    } else {
        // operator
        val lengthTypeId = current.take(1)[0]
        current = current.drop(1)
        if (lengthTypeId == '0') {
            // length
            val length = current.take(15).joinToString(separator = "").toInt(2)
            current = current.drop(15)
            val initialSize = current.size
            var consumed = 0
            while (consumed < length) {
                current = parse(current, versionCallback)
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
                current = parse(current, versionCallback)
            }
        }
    }

    return current
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
    parse(converted, {
        versionSum += it
    })
    println(versionSum)
}