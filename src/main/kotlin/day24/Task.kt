package day24

import java.io.File
import kotlin.math.round

object Task {
    fun solvePart1(filename: String, start: Double = 200000000000000.0, end: Double = 400000000000000.0) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, start, end) }

    fun solvePart2(filename: String, start: Double = 200000000000000.0, end: Double = 400000000000000.0) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, start, end) }

    fun process(lines: Sequence<String>, start: Double, end: Double) = connectEach(parse(lines))
        .mapNotNull { intersection(it.first().first, it.first().second, it.last().first, it.last().second) }
        .count { (x, y) -> x in start..end && y in start..end }

    fun connectEach(pv: Set<Pair<P, P>>) = pv
        .flatMap { pv1 ->
            pv
                .filter { it != pv1 }
                .map { pv2 -> setOf(pv1, pv2) }
        }.distinct()

    private fun parse(lines: Sequence<String>) = lines.map { line -> line.toPV() }.toSet()

    fun intersection(p1: P, v1: P, p2: P, v2: P): P? {
        val n1 = P(v1.y, -v1.x)
        val n2 = P(v2.y, -v2.x)
        val c1 = n1.x * p1.x + n1.y * p1.y
        val c2 = n2.x * p2.x + n2.y * p2.y
        val x = (c1 * n2.y - c2 * n1.y) / (n2.y * n1.x - n1.y * n2.x)
        val y = (c1 - n1.x * x) / n1.y
        return P(x, y)
            .takeIf {
                x != Double.NEGATIVE_INFINITY && x != Double.POSITIVE_INFINITY
                        && (x > p1.x && v1.x > 0 || x < p1.x && v1.x < 0)
                        && (x > p2.x && v2.x > 0 || x < p2.x && v2.x < 0)
                        && (y > p1.y && v1.y > 0 || y < p1.y && v1.y < 0)
                        && (y > p2.y && v2.y > 0 || y < p2.y && v2.y < 0)
            }
            ?.rounded()
    }
}

fun String.toPV() = split(" @ ").let { (p, v) -> p.toP() to v.toP() }

fun String.toP() = split(", ").let { (x, y, _) -> P(x.toDouble(), y.toDouble()) }

data class P(val x: Double, val y: Double) {
    override fun toString() = "($x,$y)"
    fun rounded(m: Int = 1000) = P(round(x * m) / m, round(y * m) / m)
}