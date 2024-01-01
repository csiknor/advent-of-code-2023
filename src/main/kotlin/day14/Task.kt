package day14

import java.io.File

object Task {

    /*
    The solution involves rolling all the moving rocks to north and then measuring the weight. Given the board is
    available as a list of Strings upon parsing, it is easier to roll left and right, therefore the required movement
    is implemented as a transpose and a roll left.
     */
    fun solvePart1(filename: String) =
        process(File(javaClass.getResource(filename)!!.toURI()).readLines())

    /*
    Part 2 is more complex in two ways. First, we need to roll a full cycle; Second, we need to do this a billion times.
    The first part could be solved by combinations of transposing and rolling left or right. The second part is more
    complex, byt using a hint, a cycle in the weights after each round can be detected. Using the detected cycle's start
    and length, the one billionth weight can be calculated.
     */
    fun solvePart2(filename: String, cycles: Int = 1_000_000_000) =
        process2(File(javaClass.getResource(filename)!!.toURI()).readLines(), cycles)

    private fun process(lines: List<String>) = transpose(lines).sumOf { line ->
        weighRocksLeft(rollLeft(line))
    }

    // I chose to do the fist thousand cycles and calculate their weights so that the cycle can be detected.
    private fun process2(lines: List<String>, cycles: Int) =
        generateSequence(lines) { rollCycle(it) }
            .map { transpose(it).sumOf { line -> weighRocksLeft(line) } }
            .take(1_000)
            .toList()
            .let { weights -> detectCycle(weights)
                .let { (start, length) -> weights[start + (cycles - start) % length] }
            }

    fun rollCycle(lines: List<String>): List<String> {
        val north = transpose(transpose(lines).map { rollLeft(it) })
        val west = north.map { rollLeft(it) }
        val south = transpose(transpose(west).map { rollRight(it) })
        val east = south.map { rollRight(it) }
        return east
    }

    fun weighRocksLeft(line: String) =
        line.mapIndexed { index, c -> when(c) { 'O' -> line.length - index else -> 0 } }.sum()

    fun rollLeft(s: String) = roll(s, 'O', '.')
    fun rollRight(s: String) = roll(s, '.', 'O')

    private fun roll(s: String, first: Char, last: Char) =
        s.split("#").joinToString("#") { part ->
            part.count { it == first }.let { c -> first.toString().repeat(c) + last.toString().repeat(part.length - c) }
        }

    fun transpose(patterns: List<String>) = patterns
        .fold(List(patterns.first().length) { "" }) { acc, s -> acc.zip(s.toList()).map { (a, c) -> a + c } }

    /*
    Cycle detection is implemented by finding various length of numbers repeating after a certain index till the end of
    the list.
     */
    fun detectCycle(numbers: List<Int>) =
        // I wish this was more functional, but ultimately it is a pair of nested loops.
        1.rangeUntil(numbers.size/2).asSequence()
        .flatMap { len -> 0.rangeUntil(numbers.size/2).asSequence().map { len to it } }
        .first { (len, i) ->
            (1..<(numbers.size - i) / len)
                .all { numbers.subList(i, i + len) == numbers.subList(i + it * len, i + (it + 1) * len) }
                    && numbers.subList(i, i + (numbers.size - i) % len) == numbers.subList(numbers.size - (numbers.size - i) % len, numbers.size)
        }.let { it.second to it.first }
}