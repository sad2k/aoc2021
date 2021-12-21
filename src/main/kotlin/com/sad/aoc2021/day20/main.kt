package com.sad.aoc2021.day20

import com.sad.aoc2021.loadFromResources
import com.sad.aoc2021.splitWhen

private fun mapToInt(s: String): Int {
    return s.replace('.', '0').replace('#', '1').toInt(2)
}

private fun enhance(algo: String, image: List<String>, outside: Char): Pair<List<String>, Char> {
    fun getPixel(i: Int, j: Int): Char {
        if (i < 0 || j < 0 || i >= image.size || j >= image[0].length) {
            return outside
        } else {
            return image[i][j]
        }
    }

    fun getSquareFor(i: Int, j: Int): String {
        return "" + getPixel(i - 1, j - 1) + getPixel(i - 1, j) + getPixel(i - 1, j + 1) +
                getPixel(i, j - 1) + getPixel(i, j) + getPixel(i, j + 1) +
                getPixel(i + 1, j - 1) + getPixel(i + 1, j) + getPixel(i + 1, j + 1)
    }

    val newImage = MutableList(image.size + 2) { MutableList(image[0].length + 2) { ' ' } }
    val newOutsideIdx = mapToInt(outside.toString().repeat(9))
    val newOutside = algo[newOutsideIdx]

    for (i in newImage.indices) {
        for (j in newImage[0].indices) {
            val oldI = i - 1
            val oldJ = j - 1
            val newIdx = mapToInt(getSquareFor(oldI, oldJ))
            val newCh = algo[newIdx]
            newImage[i][j] = newCh
        }
    }

    val newImageWithStr = newImage.map { it.joinToString(separator = "")}

    return Pair(newImageWithStr, newOutside)
}

fun main() {
    val input = loadFromResources("day20.txt").readLines().splitWhen { it.trim().isEmpty() }
    val algo = input[0][0]
    val image = input[1]

    // part 1
    val enhanced1 = enhance(algo, image, '.')
    val enhanced2 = enhance(algo, enhanced1.first, enhanced1.second)
    println(enhanced2.first.map { it.count { ch -> ch == '#' } }.sum())
}


