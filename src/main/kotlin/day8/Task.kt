package day8

import java.io.File
import kotlin.math.max
import kotlin.math.min

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { startingPointsPart1() } }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { startingPointsPart2(it) } }

    fun process(lines: Sequence<String>, startingPoints: (Collection<String>) -> List<String>) = lines
        .fold(emptyList<Char>() to emptyMap<String, Pair<String, String>>()) { (directions, map), line ->
            if (directions.isEmpty()) line.toList() to map
            else if (line.isEmpty()) directions to map
            else directions to map + connectionsOf(line)
        }.let { (directions, map) ->
            startingPoints(map.keys)
                .map { start ->
                    generateSequence { directions }.flatten()
                        .runningFold(start) { node, c -> map[node]!!.bySign(c) }
                        .takeWhile { notFinished(it) }
                        .count().toLong()
                }
                .reduce { a, b -> lcm(a, b) }
        }

}

fun lcm(a: Long, b: Long): Long =
    (max(a, b) to min(a, b)).let { (larger, smaller) -> doLcm(larger, larger, smaller) }

tailrec fun doLcm(curr: Long, larger: Long, smaller: Long): Long =
    if (curr % larger == 0L && curr % smaller == 0L) curr else doLcm(curr + larger, larger, smaller)

fun notFinished(node: String) = !node.endsWith('Z')

fun startingPointsPart1() = listOf("AAA")

fun startingPointsPart2(nodes: Collection<String>) = nodes.filter { it.endsWith('A') }

fun connectionsOf(line: String) = """(\w+) = \((\w+), (\w+)\)""".toRegex().find(line)!!
    .groupValues.let { (_, node, left, right) -> node to (left to right) }

fun Pair<String, String>.bySign(c: Char) = when (c) { 'L' -> first 'R' -> second else -> throw Error("Sign???") }
