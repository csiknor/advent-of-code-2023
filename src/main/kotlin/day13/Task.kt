package day13

import day10.replaceElementAt
import java.io.File

object Task {
    fun solvePart1(filename: String) =
        process(File(javaClass.getResource(filename)!!.toURI()).readLines())

    fun solvePart2(filename: String) =
        process2(File(javaClass.getResource(filename)!!.toURI()).readLines())


    fun process(patterns: List<String>) =
        parse(patterns).sumOf { pattern ->
            100 * findMirrored(pattern)//.also { println("Once $it ${it.midpoint()}") }
                .midpoint() +
                    findMirrored(transpose(pattern))//.also { println("Twice $it ${it.midpoint()}") }
                        .midpoint()
        }

    fun process2(patterns: List<String>) =
        parse(patterns)
            .sumOf {
//                println("Pattern")
                it.forEach { println(it) }
                val once = findMirrored(it)
                val twice = findMirrored(transpose(it))
                smudge(it)
                    .map { pattern ->
                        differentOrNull(findMirrored(pattern, once)/*.also { println("Once $it ${it.midpoint()}") }*/, once) to
                                differentOrNull(findMirrored(transpose(pattern), twice)/*.also { println("Twice $it ${it.midpoint()}") }*/, twice)
                    }
                    .map { (a, b) -> 100 * a.midpoint() + b.midpoint() }
                    .first { it > 0 }
            }

    private fun differentOrNull(a: Pair<Int, Int>?, b: Pair<Int, Int>?) = if (a == b) null else a

    private fun smudge(pattern: List<String>) =
        pattern.indices.flatMap { r -> pattern.first().indices.map { c -> r to c } }
            .asSequence()
            .map { (r, c) ->
                pattern//.also { println("Smudge $r, $c") }
                    .replaceElementAt(r, pattern[r].replaceRange(c.rangeTo(c), opposite(pattern[r][c])))
            }

    private fun opposite(c: Char) = when (c) {
        '.' -> "#"
        '#' -> "."
        else -> throw Error("Opposite? [$c]")
    }


    fun Pair<Int, Int>?.midpoint() = if (this == null) 0 else first + ((second - first + 1) / 2)

    fun parse(patterns: List<String>) =
        patterns.fold(listOf(emptyList<String>())) { acc, s ->
            if (s.isEmpty()) acc.plusElement(emptyList()) else acc.dropLast(1).plusElement(acc.last() + s)
        }

    fun transpose(patterns: List<String>) = patterns
        .fold(List(patterns.first().length) { "" }) { acc, s -> acc.zip(s.toList()).map { (a, c) -> a + c } }

    fun findMirroredL(pattern: List<String>, index: Int = 0): Int? =
        if (pattern.size == 1) null else
            if (mirrored(pattern)) pattern.size - 1 else findMirroredL(pattern.dropLast(1), index + 1)

    fun findMirrored(pattern: List<String>, except: Pair<Int, Int>? = null) =
        listOf(
            findMirroredL(pattern)?.let { 0 to it },
            findMirroredL(pattern.asReversed())?.let { pattern.size - 1 - it to pattern.size - 1 },
        ).firstOrNull { it != null && it != except }

    fun mirrored(list: List<String>) =
        list.isNotEmpty() && list.size % 2 == 0 &&
                list.dropLast(list.size / 2) == list.drop(list.size / 2).asReversed()

}