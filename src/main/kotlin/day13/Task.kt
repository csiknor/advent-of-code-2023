package day13

import day10.replaceElementAt
import java.io.File

object Task {

    /*
    For each pattern we look for a mirrored image from left and right and then from up and down (right is the same as
    left, but reversed, while up and down are the same as left and right after transposing the pattern). Once found the
    midpoint is calculated and the function of horizontal and vertical values is applied to get the result.
     */
    fun solvePart1(filename: String) =
        process(File(javaClass.getResource(filename)!!.toURI()).readLines())

    /*
    On top of finding the horizontal and vertical mirrors for each pattern we smudge the pattern's tiles one-by-one and
    try to find a horizontal and vertical mirror if they're different to the original one. Once found we apply the
    function is applied to get the result.
     */
    fun solvePart2(filename: String) =
        process2(File(javaClass.getResource(filename)!!.toURI()).readLines())


    fun process(patterns: List<String>) =
        parse(patterns).sumOf { pattern ->
            100 * findMirrored(pattern).midpoint() + findMirrored(transpose(pattern)).midpoint()
        }

    fun process2(patterns: List<String>) =
        parse(patterns)
            .sumOf { pattern ->
                Pair(findMirrored(pattern), findMirrored(transpose(pattern))).let { (horizontal, vertical) ->
                    smudge(pattern)
                        .map { smudged ->
                            findMirrored(smudged, horizontal) to findMirrored(transpose(smudged), vertical)
                        }
                        .map { (a, b) -> 100 * a.midpoint() + b.midpoint() }
                        .first { it > 0 }
                }
            }

    // Smudged patterns is a sequence of the original pattern flipped one tile at a time.
    private fun smudge(pattern: List<String>) =
        pattern.indices.flatMap { r -> pattern.first().indices.map { c -> r to c } }
            .asSequence()
            .map { (r, c) ->
                pattern.replaceElementAt(r, pattern[r].replaceRange(c.rangeTo(c), flip(pattern[r][c])))
            }

    private fun flip(c: Char) = requireNotNull(when (c) { '.' -> "#" '#' -> "." else -> null }) { "Unknown char: $c" }

    fun Pair<Int, Int>?.midpoint() = if (this == null) 0 else first + ((second - first + 1) / 2)

    fun parse(patterns: List<String>) =
        patterns.fold(listOf(emptyList<String>())) { acc, s ->
            if (s.isEmpty()) acc.plusElement(emptyList()) else acc.dropLast(1).plusElement(acc.last() + s)
        }

    fun transpose(patterns: List<String>) = patterns
        .fold(List(patterns.first().length) { "" }) { acc, s -> acc.zip(s.toList()).map { (a, c) -> a + c } }

    tailrec fun findMirroredL(pattern: List<String>, index: Int = 0): Int? =
        if (pattern.size == 1) null else
            if (mirrored(pattern)) pattern.lastIndex else findMirroredL(pattern.dropLast(1), index + 1)

    fun findMirrored(pattern: List<String>, except: Pair<Int, Int>? = null) =
        listOf(
            findMirroredL(pattern)?.let { 0 to it },
            findMirroredL(pattern.asReversed())?.let { pattern.lastIndex - it to pattern.lastIndex },
        ).firstOrNull { it != null && it != except }

    fun mirrored(list: List<String>) =
        list.isNotEmpty() && list.size % 2 == 0 &&
                list.dropLast(list.size / 2) == list.drop(list.size / 2).asReversed()

}