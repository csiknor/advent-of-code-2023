package day8

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines
        .fold(emptyList<Char>() to emptyMap<String, Pair<String, String>>()) { (directions, map), line ->
            if (directions.isEmpty()) line.toList() to map
            else if (line.isEmpty()) directions to map
            else directions to  map + connectionsOf(line)
        }.let { (directions, map) ->
            generateSequence { directions }.flatten()
                .runningFold("AAA") { node, c ->
                    map[node]!!.bySign(c)
                }
                .takeWhile { it != "ZZZ" }
                .count()
                .also { println("Count: $it") }
        }

}

fun connectionsOf(line: String) =
    """(\w+) = \((\w+), (\w+)\)""".toRegex().find(line)!!.groupValues.let { (_, node, left, right) ->
       node to (left to right)
    }

fun Pair<String, String>.bySign(c: Char) = when (c) {
    'L' -> first
    'R' -> second
    else -> throw Error("Sign???")
}
