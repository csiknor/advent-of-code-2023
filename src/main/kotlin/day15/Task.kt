package day15

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    fun process(lines: Sequence<String>) = lines.map { line ->
        line.split(",").map { s -> hash(s) }.sum()
    }.sum()

    fun process2(lines: Sequence<String>) = lines.map { line ->
        line.split(",").fold(List(256) { LinkedHashMap<String, Int>() }) { acc, command ->
            """(\w+)([=-])(\d)*""".toRegex().find(command)?.let { match ->
                acc[hash(match.groupValues[1])].let { box ->
                    if (match.groupValues[2] == "-") {
                        box.remove(match.groupValues[1])
                    } else {
                        box.put(match.groupValues[1], match.groupValues[3].toInt())
                    }
                }
            }

            acc
        }.mapIndexed { box, lenses ->
            lenses.entries.mapIndexed { index, lens ->
                strength(box, index, lens.value)
            }.sum()
        }.sum()
    }.sum()

    fun strength(box: Int, index: Int, focalLength: Int) = (1 + box) * (1 + index) * focalLength

    fun hash(s: String): Int = s.fold(0) { acc, c -> (acc + c.code) * 17 % 256 }
}