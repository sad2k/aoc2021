package com.sad.aoc2021.day18

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.readFirstLine
import kotlin.IllegalArgumentException

abstract class SnailfishNumber(var parent: SnailfishPair?)

class SnailfishPair(var left: SnailfishNumber?, var right: SnailfishNumber?, parent: SnailfishPair?) :
    SnailfishNumber(parent) {
    constructor(parent: SnailfishPair?) : this(null, null, parent) {
    }

    override fun toString(): String {
        return "[$left,$right]"
    }
}

class SnailfishRegular(var value: Int, parent: SnailfishPair?) : SnailfishNumber(parent) {
    override fun toString(): String {
        return "$value"
    }
}

fun parse(s: String): SnailfishNumber {
    val stack = mutableListOf<SnailfishPair>()
    var currentPair: SnailfishPair? = null
    var leftSet = false
    for (i in s.indices) {
        val ch = s[i]
        if (ch == '[') {
            val newPair = SnailfishPair(currentPair)
            if (currentPair != null) {
                if (leftSet) {
                    currentPair.right = newPair
                } else {
                    currentPair.left = newPair
                }
                stack.add(currentPair)
            }
            currentPair = newPair
            leftSet = false
        } else if (ch == ']') {
            if (i != s.length - 1) {
                currentPair = stack.removeAt(stack.size - 1)
            }
            leftSet = false
        } else if (ch == ',') {
            leftSet = true
        } else if ("0123456789".contains(ch)) {
            val number = SnailfishRegular(ch.digitToInt(), currentPair)
            if (currentPair != null) {
                if (leftSet) {
                    currentPair.right = number
                } else {
                    currentPair.left = number
                }
            } else {
                throw IllegalArgumentException("currentPair is null when parsing a number at pos ${i}: ${ch}")
            }
        } else {
            throw IllegalArgumentException("unsupported char: ${ch}")
        }
    }
    return currentPair!!
}

fun reduce(num: SnailfishNumber) {
    var reductionsMade = false
    do {
        reductionsMade = tryReduce(num, false)
        if (!reductionsMade) {
            reductionsMade = tryReduce(num, true)
        }
    } while (reductionsMade)
}

fun goLeftUntilRegular(num: SnailfishPair): SnailfishRegular? {
    if (num.parent == null) {
        return null
    } else {
        if (num.parent!!.left === num) {
            return goLeftUntilRegular(num.parent!!)
        } else {
            // go right until regular
            var cur = num.parent!!.left
            while (cur is SnailfishPair) {
                cur = cur.right
            }
            return cur as SnailfishRegular
        }
    }
}

fun goRightUntilRegular(num: SnailfishPair): SnailfishRegular? {
    if (num.parent == null) {
        return null
    } else {
        if (num.parent!!.right === num) {
            return goRightUntilRegular(num.parent!!)
        } else {
            // go left until regular
            var cur = num.parent!!.right
            while (cur is SnailfishPair) {
                cur = cur.left
            }
            return cur as SnailfishRegular
        }
    }
}

fun explode(num: SnailfishPair) {
    val left = num.left as SnailfishRegular
    val right = num.right as SnailfishRegular

    val leftRegular = goLeftUntilRegular(num)
    var rightRegular = goRightUntilRegular(num)
//    println("left regular: ${leftRegular}")
//    println("right regular: ${rightRegular}")

    if (leftRegular != null) {
        leftRegular.value = leftRegular.value + left.value
    }
    if (rightRegular != null) {
        rightRegular.value = rightRegular.value + right.value
    }
    val zero = SnailfishRegular(0, num.parent)
    if (num.parent == null) {
        throw IllegalStateException("didn't expect null parent here...")
    } else {
        if (num.parent!!.left === num) {
            num.parent!!.left = zero
        } else {
            num.parent!!.right = zero
        }
    }
}

fun tryReduce(num: SnailfishNumber, allowSplits: Boolean): Boolean {
    fun tryReduce0(num: SnailfishNumber, depth: Int): Boolean {
        return when (num) {
            is SnailfishRegular -> {
//                println("found ${num} at depth ${depth}")
                if (allowSplits) {
                    if (num.value >= 10) {
//                        println("will split ${num}")
                        val newPair = SnailfishPair(num.parent)
                        newPair.left = SnailfishRegular(Math.floor(num.value.toDouble() / 2.0).toInt(), newPair)
                        newPair.right = SnailfishRegular(Math.ceil(num.value.toDouble() / 2.0).toInt(), newPair)
                        if (num.parent != null) {
                            if (num.parent!!.left === num) {
                                num.parent!!.left = newPair
                            } else {
                                num.parent!!.right = newPair
                            }
                        } else {
                            throw IllegalStateException("null parent isnt expected here: ${num}")
                        }
                        return true
                    }
                }
                false
            }
            is SnailfishPair -> {
//                println("found pair ${num} at depth ${depth}")
                if (depth >= 4 && num.left is SnailfishRegular && num.right is SnailfishRegular) {
//                    println("will explode ${num}")
                    explode(num)
                    return true
                }
                tryReduce0(num.left!!, depth + 1) || tryReduce0(num.right!!, depth + 1)
            }
            else -> throw IllegalArgumentException("unsupported number: ${num}")
        }
    }

    return tryReduce0(num, 0)
}

fun add(num1: SnailfishNumber, num2: SnailfishNumber): SnailfishNumber {
    val added = SnailfishPair(null)
    num1.parent = added
    num2.parent = added
    added.left = num1
    added.right = num2
    reduce(added)
    return added
}

fun magnitude(num: SnailfishNumber): Long {
    return when (num) {
        is SnailfishPair -> 3 * magnitude(num.left!!) + 2 * magnitude(num.right!!)
        is SnailfishRegular -> num.value.toLong()
        else -> throw IllegalArgumentException("unsupported number: ${num}")
    }
}

fun main() {
    val input = loadFromResources("day18.txt").readLines().map(::parse)
    println(magnitude(input.reduce { acc, num -> add(acc, num) }))
}