package day15

import java.io.File

object Task {

    /*
    The solution is pretty straightforward after parsing the input by executing the specified hash algorithm on the
    steps and cumming them.
     */
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    /*
    The second part requires the parsing of each step into their name, operation and optional focal length. In order to
    calculate the strength of each box, we need to group the steps by the hash of their name, and either remove or add
    the focal length to each box. The solution leverages that the `LinkedHashMap` maintains the order the elements are
    put into the map, so that even after removing some of them their index remains in order.
     */
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    fun process(lines: Sequence<String>) =
        lines.sumOf { line -> line.split(",").sumOf { s -> hash(s) } }

    fun process2(lines: Sequence<String>) =
        lines.sumOf { line ->
            line.splitToSequence(",")
                .mapNotNull { it.toStep() }
                .groupingBy { hash(it.name) }
                .fold(emptyMap<String, Int>()) { acc, (name, operation, focalLength) ->
                    if (operation == "-") acc - name
                    else acc + (name to focalLength!!)
                }.map { (box, lenses) ->
                    lenses.values.mapIndexed { index, focalLength -> strength(box, index, focalLength) }.sum()
                }.sum()
        }

    fun String.toStep() = """(\w+)([=-])(\d)*""".toRegex()
        .find(this)?.groupValues?.let { (_, name, operation, focalLength) ->
            Step(name, operation, focalLength.takeIf { it.isNotEmpty() }?.toInt())
        }

    fun strength(box: Int, index: Int, focalLength: Int) = (1 + box) * (1 + index) * focalLength

    fun hash(s: String): Int = s.fold(0) { acc, c -> (acc + c.code) * 17 % 256 }
}

data class Step(val name: String, val operation: String, val focalLength: Int?)
