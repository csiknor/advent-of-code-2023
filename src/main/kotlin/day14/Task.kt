package day14

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        process(File(javaClass.getResource(filename)!!.toURI()).readLines())

    fun solvePart2(filename: String, cycles: Int = 1_000_000_000) =
        process2(File(javaClass.getResource(filename)!!.toURI()).readLines(), cycles)

    private fun process(lines: List<String>) = transpose(lines).sumOf { line ->
        weighRocks(rollLeft(line))
    }

    private fun process2(lines: List<String>, cycles: Int) =
        generateSequence(lines) { rollCycle(it) }
            .map { transpose(it).sumOf { line -> weighRocks(line) } }
            .take(1_000)
            .toList()
            .let { weights -> detectCycle(weights)!!
                .let { (start, length) -> weights[start + (cycles - start) % length] }
            }

    fun rollCycle(lines: List<String>): List<String> {
        val north = transpose(transpose(lines).map { rollLeft(it) })
        val west = north.map { rollLeft(it) }
        val south = transpose(transpose(west).map { rollRight(it) })
        val east = south.map { rollRight(it) }
        return east
    }

    fun weighRocks(line: String) = line.mapIndexed { index, c -> when(c) { 'O' -> line.length - index else -> 0 } }.sum()

    fun rollLeft(s: String) = roll(s, 'O', '.')
    fun rollRight(s: String) = roll(s, '.', 'O')

    private fun roll(s: String, first: Char, last: Char) = s.split("#").map { part ->
        part.count { it == first }.let { c -> first.toString().repeat(c) + last.toString().repeat(part.length-c) }
    }.joinToString("#")

    fun transpose(patterns: List<String>) = patterns
        .fold(List(patterns.first().length) { "" }) { acc, s -> acc.zip(s.toList()).map { (a, c) -> a + c } }

    fun detectCycle(numbers: List<Int>): Pair<Int, Int>? {
        for (len in 1..<(numbers.size/2)) {
            for (i in 0..<(numbers.size/2)) {
                val cycleCount = (numbers.size - i) / len - 1
                val remainingCount = (numbers.size - i) % len
                if ((1..cycleCount).all { numbers.subList(i, i+len) == numbers.subList(i+it*len, i+it*len+len) }
                    && numbers.subList(i, i+remainingCount) == numbers.subList(numbers.size-remainingCount, numbers.size)
                ) return i to len
            }
        }
        return null
    }
}