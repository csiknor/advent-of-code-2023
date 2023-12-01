package day1

import java.io.File

object Task1 {

    fun solve1() {
        File(Task1.javaClass.getResource("sample1.txt")!!.toURI()).readLines().asSequence()
            .onEach { print("$it ") }
            .map { it.replace("[a-z]".toRegex(), "") }
            .onEach { println("$it ") }
            .sumOf { "${it.first()}${it.last()}".toInt() }
            .let { println(it) }
    }
}