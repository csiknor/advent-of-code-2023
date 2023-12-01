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

    private val digits = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
    fun solve2() {
        File(Task1.javaClass.getResource("sample2.txt")!!.toURI()).readLines()
            .asSequence()
            .onEach { print("$it ") }
            .map { replaceInOrder(it) }
            .onEach { print("$it ") }
            .map { "${it.first()}${it.last()}".toInt() }
            .onEach { println("$it ") }
            .sum()
            .let { println(it) }
    }

    private fun replaceInOrder(s: String) =
        digits.keys.plus((1..9).map { it.toString() }).asSequence()
            .flatMap { it.toRegex().findAll(s) }
            .sortedBy { it.range.first }
            .map { it.value }
            .map { digits[it]?.toString() ?: it }
            .joinToString("")
}