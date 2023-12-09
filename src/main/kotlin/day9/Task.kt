package day9

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { predictLast(it) } }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { predictFirst(it) } }

    fun process(lines: Sequence<String>, predictor: (List<List<Long>>) -> Long) = parse(lines)
        .map { generateDifferences(listOf(it)) }
        .map { predictor(it) }
        .sum()

    private fun parse(lines: Sequence<String>) = lines.map { line -> line.split(' ').map { it.toLong() } }
}

fun predictFirst(series: List<List<Long>>) = series.map { it.first() }.reduceRight { a, b -> a - b }
fun predictLast(series: List<List<Long>>) = series.map { it.last() }.reduce { a, b -> a + b }

fun generateDifferences(curr: List<List<Long>>): List<List<Long>> = if (curr.last().all { it == 0L }) curr
else generateDifferences(curr.plusElement(curr.last().windowed(2) { (a, b) -> b - a }))
