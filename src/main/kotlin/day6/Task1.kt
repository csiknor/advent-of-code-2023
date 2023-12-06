package day6

import java.io.File

object Task1 {

    // We parse the input into two lists and zip them together so that each race is represented. We calculate all the
    // distances for each amount of seconds we hold the button, and filter for those that beat the record. We multiply
    // these together to get the final result.
    fun solve(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines.toList()
        .let { (first, second) -> parseLabeledNumberList(first).zip(parseLabeledNumberList(second)) }
        .map { (time, distance) -> 1.rangeTo(time).map { hold -> travelDistance(hold, time) }.count { it > distance } }
        .reduce { a, b -> a * b }
        .also { println("Result: $it ") }
}

fun travelDistance(hold: Long, time: Long) = hold * (time - hold)

fun parseLabeledNumberList(input: String): List<Long> = input.split(""":\s*""".toRegex()).last().split("""\s+""".toRegex()).map { it.toLong() }
