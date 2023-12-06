package day6

import java.io.File

object Task2 {
    fun solve(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines.toList()
        .map { parseLabeledNumberListWithBadKerning(it) }
        .let { (time, distance) -> 1.rangeTo(time).map { hold -> travelDistance(hold, time) }.count { it > distance } }
        .also { println("Result: $it ") }
}

fun parseLabeledNumberListWithBadKerning(input: String): Long = input
    .split(""":\s*""".toRegex()).last()
    .replace(" ", "")
    .toLong()
