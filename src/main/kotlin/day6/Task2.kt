package day6

import java.io.File

object Task2 {

    // Similarly to the previous task we parse the input, but in this case we combine the numbers into a single number
    // by ignoring any whitespace in between them. The rest of the solution is the same, although, since we only have a
    // single race we don't need to multiply the results.
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
