package day14

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        process(File(javaClass.getResource(filename)!!.toURI()).readLines())

    fun solvePart2(filename: String) =
        process(File(javaClass.getResource(filename)!!.toURI()).readLines())

    fun process(lines: List<String>) = transpose(lines).map { line ->
        weighRocks(rollLeft(line))
    }.sum()

    fun weighRocks(line: String) = line.mapIndexed { index, c -> when(c) { 'O' -> line.length - index else -> 0 } }.sum()

    fun rollLeft(s: String) = s.split("#").map { part ->
        part.count { it == 'O' }.let { c -> "O".repeat(c) + ".".repeat(part.length-c) }
    }.joinToString("#")

    fun transpose(patterns: List<String>) = patterns
        .fold(List(patterns.first().length) { "" }) { acc, s -> acc.zip(s.toList()).map { (a, c) -> a + c } }
}