package day9

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = parse(lines)
        .map { generateDifferences(listOf(it)) }
        .map { predictLast(it) }
        .sum()

    private fun parse(lines: Sequence<String>) = lines.map { it.split(' ').map { it.toLong() } }
}

fun predictLast(series: List<List<Long>>) = series.reversed().sumOf { it.last() }

fun generateDifferences(curr: List<List<Long>>): List<List<Long>> = if (curr.last().sum() == 0L) curr
else generateDifferences(curr.plusElement(curr.last().windowed(2) { (a, b) -> b - a }))
