package day18

import java.io.File
import kotlin.math.abs

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(parse(line)) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line ->
            process(parse(line).map { it.alternative() })
        }

    // Pick's theorem
    fun process(digs: Sequence<Dig>) = vertices(digs)
        .let { vertices -> area(vertices) + distance(vertices) / 2L + 1L }

    private fun vertices(digs: Sequence<Dig>) = digs
        .fold(listOf(P(0, 0))) { acc, dig ->
            acc + (acc.last() + dig.dir.delta * dig.count)
        }

    // Manhattan distance
    private fun distance(vertices: List<P>) =
        vertices.windowed(2) { (v1, v2) -> abs(v1.row - v2.row) + abs(v1.col - v2.col) }.sum()

    // Shoelace formula
    private fun area(vertices: List<P>) =
        (vertices.windowed(2) { (v1, v2) -> v1.col.toLong() * v2.row.toLong() }.sum() -
                vertices.windowed(2) { (v1, v2) -> v1.row.toLong() * v2.col.toLong() }.sum()) / 2L

    private fun parse(lines: Sequence<String>) = lines.map { line ->
        line.split(" ").let { (dir, count, color) ->
            Dig(Dir.valueOf(dir), count.toInt(), color.substring(1, color.lastIndex))
        }
    }
}

data class P(val row: Int, val col: Int) {
    operator fun plus(delta: P) = P(row + delta.row, col + delta.col)
    operator fun times(value: Int) = P(row * value, col * value)
}

enum class Dir(val delta: P) {
    R(P(0, 1)),
    D(P(1, 0)),
    L(P(0, -1)),
    U(P(-1, 0)),
}

data class Dig(val dir: Dir, val count: Int, val color: String? = null) {
    fun alternative() = color?.let { color ->
        Dig(
            Dir.entries[color.last().toString().toInt()],
            color.substring(1, 6).toInt(16)
        )
    } ?: throw IllegalStateException("Empty color")
}