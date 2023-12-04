package day4

import java.io.File
import kotlin.math.pow

data class Card(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {

    // Matching numbers are simply the intersection of the winning numbers and the numbers we have
    fun matchingNumbers() = winningNumbers.toSet().intersect(numbers.toSet())

    // Points are calculated as a power of two unless no matching numbers
    fun pointsWin() = matchingNumbers().count().let { if (it > 0) 2.0.pow(it - 1).toInt() else 0 }

    // Count of matching numbers
    fun matchingCount() = matchingNumbers().count()
}

object Task1 {

    // We parse each line of the input into a Card. Then we sum the points win on each card
    fun solve(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines
        .map { cardOf(it) }
        .onEach { println("$it ${it.matchingNumbers()} -> ${it.pointsWin()}") }
        .sumOf { it.pointsWin() }
        .also { println("Sum: $it") }

    // Card parsing is done using a regular expression
    fun cardOf(input: String) = """Card\s+(\d+): ([\d ]+) \| ([\d ]+)""".toRegex().find(input)!!
        .let { match ->
            Card(
                match.groupValues[1].toInt(),
                numbersOf(match.groupValues[2].trim()),
                numbersOf(match.groupValues[3].trim()),
            )
        }

    // Numbers are whitespace separated
    private fun numbersOf(input: String) =
        input.split("""\s+""".toRegex()).map { s -> s.toInt() }

}