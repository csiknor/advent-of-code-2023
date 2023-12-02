package day1

import java.io.File

object Task1 {

    // The idea is to get rid of the alphabetic characters and create numbers using the first and last of the remaining
    // digits. E.g.:
    //   1abc2 -> 12 -> 12
    //   pqr3stu8vwx -> 38 -> 38
    //   a1b2c3d4e5f -> 12345 -> 15
    //   treb7uchet -> 7 -> 77
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