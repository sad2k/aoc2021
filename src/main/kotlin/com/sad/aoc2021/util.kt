package com.sad.aoc2021

import java.io.File

fun loadFromResources(fileName: String): File {
    return File(object {}.javaClass.getResource("/${fileName}")!!.file)
}

fun File.readFirstLine(): String = this.readLines()[0]