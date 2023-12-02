package day1

import java.io.File

object Task2 {
    private val digits = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
    fun solve() {
        // Loads the input file line by line
        File(Task2.javaClass.getResource("sample2.txt")!!.toURI()).readLines()
            .asSequence()
            .onEach { print("$it ") }
            // Replacing recognised digits and numbers with the corresponding digit in the order they appear on the line
            .map { replaceInOrder(it) }
            .onEach { print("$it ") }
            // The remaining string consists of digits only, and a new number is constructed using its first and last
            // digits.
            // Note that if a string is made of only a single digit, then its first and last characters are the same.
            .map { "${it.first()}${it.last()}".toInt() }
            .onEach { println("$it ") }
            // Calculating the sum of the constructed numbers
            .sum()
            .let { println(it) }
    }

    private fun replaceInOrder(s: String) =
        // Combining the numbers and the digits together into a single list
        digits.keys.plus((1..9).map { it.toString() }).asSequence()
            // Using each one of them as a regular expression, we generate match results
            // We have as many match results as many matches for any number or digit
            .flatMap { it.toRegex().findAll(s) }
            // We sort the matches by their index in the string
            .sortedBy { it.range.first }
            // Use the match value, which is the number or the digit found in the string
            .map { it.value }
            // We convert it to a digit using the mapping or the value itself
            .map { digits[it]?.toString() ?: it }
            // We join the digits into a single string
            .joinToString("")

}