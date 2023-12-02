package day1

import java.io.File

object Task1 {

    fun solve() {
        // Loads the input file line by line
        File(Task1.javaClass.getResource("sample1.txt")!!.toURI()).readLines().asSequence()
            .onEach { print("$it ") }
            // Each line's alphabetic characters are removed
            .map { it.replace("[a-z]".toRegex(), "") }
            .onEach { println("$it ") }
            // The remaining string consists of digits only, and a new number is constructed using its first and last
            // digits and then summing all up. Note that if a string is made of only a single digit, then its first and
            // last characters are the same.
            .sumOf { "${it.first()}${it.last()}".toInt() }
            .let { println(it) }
    }
}