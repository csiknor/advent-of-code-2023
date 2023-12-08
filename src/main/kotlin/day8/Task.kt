package day8

import java.io.File
import kotlin.math.max
import kotlin.math.min

object Task {

    // After parsing the input into a directions list and a navigation map, we start from the starting point 'AAA' and
    // go through the map as long as we find the finish node 'ZZZ'. Since we need to restart the directions list if we
    // run out of next steps, we generate an infinite sequence of them and map each direction to the next node based on
    // the previous node using the navigation map. We count how many steps we've done for the result.
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { startingPointsPart1() } }

    // Extending the first part by starting from multiple nodes and calculating the number of steps for each of them.
    // Since we need to arrive to the finish from all the source nodes at the same time, we need to calculate the lowest
    // common multiple of them.
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { startingPointsPart2(it) } }

    fun process(lines: Sequence<String>, startingPoints: (Collection<String>) -> List<String>) = parse(lines)
        .let { (directions, map) ->
            startingPoints(map.keys)
                .map { start ->
                    generateSequence { directions }.flatten()
                        .runningFold(start) { node, c -> map[node]!!.bySign(c) }
                        .takeWhile { notFinished(it) }
                        .count().toLong()
                }
                .reduce { a, b -> lcm(a, b) }
        }

    private fun parse(lines: Sequence<String>) = lines
            .fold(emptyList<Char>() to emptyMap<String, Pair<String, String>>()) { (directions, map), line ->
                if (directions.isEmpty()) line.toList() to map
                else if (line.isEmpty()) directions to map
                else directions to map + connectionsOf(line)
            }

}

// Calculating the lowest common multiple is done adding the larger number to itself until we reach a common multiple.
// It is implemented as a tail recursive function.
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
