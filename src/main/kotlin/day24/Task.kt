package day24

import java.io.File
import kotlin.math.round

object Task {
    fun solvePart1(filename: String, start: Double = 200000000000000.0, end: Double = 400000000000000.0) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, start, end) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    private fun process2(lines: Sequence<String>) =
        parse(lines).let { rocks ->
            calcPlainVector(rocks)
                .let { (x, y, vx, vy) -> PV(P(round(x), round(y)), P(round(vx), round(vy))) }
                .let { pv ->
                    calcElev(rocks, pv.p.x, pv.v.x)
                        .let { (z, zv) -> PV(pv.p.copy(z = round(z)), pv.v.copy(z = round(zv))) }
                }.let { pv -> (pv.p.x + pv.p.y + pv.p.z).toLong() }
        }

    private fun calcElev(rocks: Collection<PV>, x: Double, vx: Double) =
        rocks.windowed(2) { (r1, r2) ->
            arrayOf(
                r1.v.x - r2.v.x,
                r2.p.x - r1.p.x,
            ) to (-r1.p.x * r1.v.z + r1.p.z * r1.v.x + r2.p.x * r2.v.z - r2.p.z * r2.v.x
                    - ((r2.v.z - r1.v.z) * x) - ((r1.p.z - r2.p.z) * vx))
        }.take(2).unzip().let { (coeff, rhs) ->
            gaussianElimination(coeff.toTypedArray(), rhs.toTypedArray()).let { (i1, i2) -> i1 to i2 }
        }

    private fun calcPlainVector(rocks: Collection<PV>) =
        rocks.windowed(2) { (r1, r2) ->
            arrayOf(
                r2.v.y - r1.v.y,
                r1.v.x - r2.v.x,
                r1.p.y - r2.p.y,
                r2.p.x - r1.p.x,
            ) to (-r1.p.x * r1.v.y + r1.p.y * r1.v.x + r2.p.x * r2.v.y - r2.p.y * r2.v.x)
        }.take(4).unzip().let { (coeff, rhs) ->
            gaussianElimination(coeff.toTypedArray(), rhs.toTypedArray())
        }

    private fun gaussianElimination(coeff: Array<Array<Double>>, rhs: Array<Double>): Array<Double> {
        for (i in coeff.indices) {
            val pivot = coeff[i][i]
            // Normalize row i
            for (j in coeff.indices) {
                coeff[i][j] /= pivot
            }
            rhs[i] /= pivot
            // Sweep using row i
            for (k in coeff.indices) {
                if (k != i) {
                    val factor = coeff[k][i]
                    for (j in coeff.indices) {
                        coeff[k][j] -= factor * coeff[i][j]
                    }
                    rhs[k] -= factor * rhs[i]
                }
            }
        }
        return rhs
    }


    fun process(lines: Sequence<String>, start: Double, end: Double) = connectEach(parse(lines))
        .mapNotNull { intersection(it.first().p, it.first().v, it.last().p, it.last().v) }
        .count { (x, y) -> x in start..end && y in start..end }

    fun connectEach(pv: Collection<PV>) = pv
        .flatMap { pv1 ->
            pv
                .filter { it != pv1 }
                .map { pv2 -> setOf(pv1, pv2) }
        }.distinct()

    private fun parse(lines: Sequence<String>) = lines.map { line -> line.toPV() }.toList()

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

fun String.toPV() = split(" @ ").let { (p, v) -> PV(p.toP(), v.toP()) }

fun String.toP() = split(", ").let { (x, y, z) -> P(x.toDouble(), y.toDouble(), z.toDouble()) }

data class PV(val p: P, val v: P)

data class P(val x: Double, val y: Double, val z: Double = 0.0) {
    override fun toString() = "($x,$y,$z)"
    fun rounded(m: Int = 1000) = P(round(x * m) / m, round(y * m) / m, round(z * m) / m)
}