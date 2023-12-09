package day9

import java.io.File

object Task {

    // We parse the input into a sequence of number lists. For each line we generate the differences, which is a
    // recursive process until we reach all zero difference, and then predict the last value. The prediction is based on
    // the last elements of each difference list, we just need to add all of them to the original one. Finally, we sum
    // the predicted value for all number lists in the sequence.
    // Note: we expect that the numbers in the input lists are in ascending order.
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { predictLast(it) } }

    // In the second part only the way of prediction changes. Given we need to predict the first number, we need to
    // consider the first elements of each difference list, and subtract each from the subtraction of the previous
    // elements.
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { predictFirst(it) } }

    fun process(lines: Sequence<String>, predictor: (List<List<Long>>) -> Long) = parse(lines)
        .map { generateDifferences(listOf(it)) }
        .map { predictor(it) }
        .sum()

    private fun parse(lines: Sequence<String>) = lines.map { line -> line.split(' ').map { it.toLong() } }
}

// Using the last elements of the differences we add them to the last element: A + d1 + d2 + d3
fun predictLast(series: List<List<Long>>) = series.map { it.last() }.reduce { a, b -> a + b }
// USing the first elements of the differences we subtract them from the previous subtraction: A - (d1 - (d2 - d3))
fun predictFirst(series: List<List<Long>>) = series.map { it.first() }.reduceRight { a, b -> a - b }

// The differences are generated into the last element of the list, so that it accumulates a list of lists, starting
// with the original list of numbers.
tailrec fun generateDifferences(curr: List<List<Long>>): List<List<Long>> = if (curr.last().all { it == 0L }) curr
else generateDifferences(curr.plusElement(curr.last().windowed(2) { (a, b) -> b - a }))
