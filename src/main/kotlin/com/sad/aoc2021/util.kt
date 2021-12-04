package com.sad.aoc2021

import java.io.File

fun loadFromResources(fileName: String): File {
    return File(object {}.javaClass.getResource("/${fileName}")!!.file)
}

fun File.readFirstLine(): String = this.readLines()[0]

fun <T> List<T>.splitWhen(predicate: (T) -> Boolean): List<List<T>> {
    val res = mutableListOf<List<T>>()
    var currentList: MutableList<T>? = null
    for (el in this) {
        if (predicate(el)) {
            if (currentList != null) {
                res.add(currentList)
            }
            currentList = null
        } else {
            if (currentList == null) {
                currentList = mutableListOf()
            }
            currentList.add(el)
        }
    }
    if (currentList != null) {
        res.add(currentList)
    }
    return res
}