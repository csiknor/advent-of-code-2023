package day11

import java.io.File
import kotlin.math.abs

data class Coord(val row: Long, val col: Long)

object Task {

    fun solvePart1(filename: String, rate: Long = 2) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, rate) }

    fun solvePart2(filename: String, rate: Long = 1_000_000) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, rate) }

    fun process(lines: Sequence<String>, rate: Long) = connectEach(expand(parse(lines), rate))
        .map { set -> set.first() to set.last() }
        .sumOf { (a, b) ->
            (abs(a.row - b.row) + abs(a.col - b.col)).also { println("$a, $b -> $it") }
        }

    fun connectEach(coords: List<Coord>) = coords
        .flatMap { p1 ->
            coords
                .filter { it != p1 }
                .map { p2 -> setOf(p1, p2) }
        }.distinct()

    fun expand(coords: List<Coord>, rate: Long) = coords.let {
        expandNumbers(coords.map { it.row }.toSet().sorted(), rate).associate { it } to
                expandNumbers(coords.map { it.col }.toSet().sorted(), rate).associate { it }
    }.let { (rowsExpanded, colsExpanded) ->
        coords.map { (row, col) -> Coord(rowsExpanded[row]!!, colsExpanded[col]!!) }
    }

    fun expandNumbers(nums: Collection<Long>, rate: Long) = nums
        .runningFold(0L to -1L) { (acc, prev), curr ->
            Pair(acc + (curr - prev - 1) * (rate - 1), curr)
        }.map { it.second to (it.first + it.second) }.drop(1)

    fun parse(lines: Sequence<String>) = lines.map { line ->
        """#""".toRegex().findAll(line)
            .map { match -> match.range.first }
    }.flatMapIndexed { index, sequence -> sequence.map { Coord(index.toLong(), it.toLong()) } }.toList()

}