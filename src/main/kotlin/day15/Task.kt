package day15

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines.map { line->
        line.split(",").map { s -> hash(s) }.sum()
    }.sum()

    fun hash(s: String): Int = s.fold(0) { acc, c -> (acc + c.code) * 17 % 256 }
}