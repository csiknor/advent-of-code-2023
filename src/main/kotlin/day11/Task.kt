package day11

import java.io.File
import kotlin.math.abs

data class Coord(val row: Long, val col: Long)

object Task {

    // The solution is based on parsing the input into a list of coordinates of each galaxy, then extending the space
    // among them. Then each galaxy is connected to the rest, and the number of steps is calculated for each pair, and
    // finally summed.
    fun solvePart1(filename: String, rate: Long = 2) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, rate) }

    // Based on the first part, we just change the rate of the space expansion from 2 to 1 million and we get the
    // result.
    fun solvePart2(filename: String, rate: Long = 1_000_000) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, rate) }

    fun process(lines: Sequence<String>, rate: Long) = connectEach(expand(parse(lines), rate))
        .map { set -> set.first() to set.last() }
        .sumOf { (a, b) ->
            (abs(a.row - b.row) + abs(a.col - b.col))
        }

    // For each coordination we map all the other coordinates and store them in a set. This list of sets is then used to
    // deduplicate.
    fun connectEach(coords: List<Coord>) = coords
        .flatMap { p1 ->
            coords
                .filter { it != p1 }
                .map { p2 -> setOf(p1, p2) }
        }.distinct()

    // Expanding the space between the galaxies involves first expanding the rows and then expanding the columns. Both
    // are done by considering all the row and column indexes of the galaxies respectively. The expanded numbers then
    // used to define the new coordinates of each galaxy.
    fun expand(coords: List<Coord>, rate: Long) = coords.let {
        expandNumbers(coords.map { it.row }.toSet().sorted(), rate).associate { it } to
                expandNumbers(coords.map { it.col }.toSet().sorted(), rate).associate { it }
    }.let { (rowsExpanded, colsExpanded) ->
        coords.map { (row, col) -> Coord(rowsExpanded[row]!!, colsExpanded[col]!!) }
    }

    // Expanding the numbers at a given rate involves calculating the numbers between two adjacent numbers and
    // multiplying it with the rate.
    fun expandNumbers(nums: Collection<Long>, rate: Long) = nums
        .runningFold(0L to -1L) { (acc, prev), curr ->
            Pair(acc + (curr - prev - 1) * (rate - 1), curr)
        }.map { it.second to (it.first + it.second) }.drop(1)

    fun parse(lines: Sequence<String>) = lines.map { line ->
        """#""".toRegex().findAll(line)
            .map { match -> match.range.first }
    }.flatMapIndexed { index, sequence -> sequence.map { Coord(index.toLong(), it.toLong()) } }.toList()

}