package day6

import java.io.File

object Task1 {
    fun solve(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines.toList()
        .let { (first, second) -> parseLabeledNumberList(first).zip(parseLabeledNumberList(second)) }
        .map { (time, distance) -> 1.rangeTo(time).map { hold -> travelDistance(hold, time) }.count { it > distance } }
        .reduce { a, b -> a * b }
        .also { println("Result: $it ") }
}

fun travelDistance(hold: Int, time: Int) = hold * (time - hold)

fun parseLabeledNumberList(input: String): List<Int> = input.split(""":\s*""".toRegex()).last().split("""\s+""".toRegex()).map { it.toInt() }
