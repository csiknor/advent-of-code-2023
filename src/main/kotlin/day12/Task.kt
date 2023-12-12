package day12

import java.io.File
import kotlin.math.min

data class R(val damaged: String, val counts: List<Int>)

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, 5) }

    fun process(lines: Sequence<String>, rate: Int = 1) = par(lines, rate).mapIndexed { index, r ->
        println(index)
        arr(r)
    }.sum()

    val cache: MutableMap<R, Long> = mutableMapOf()

    fun arr(r: R, level: Int = 0): Long {
        val v = cache[r]
        if (v!=null) return v

        fun p(s: String) = Unit //println("  ".repeat(level) + s)

        return (if (r.damaged.isEmpty()) 1L.also { p("Empty $r") } else
            tryReduce(r)?.also { p("Reduced $r -> $it") }?.let {
                if (it.damaged.isEmpty()) 1L.also { p("Empty2 $it") }
                else
                    arr(it.copy(damaged = it.damaged.replaceFirstChar { '#' }), level + 1) +
                            arr(it.copy(damaged = it.damaged.replaceFirstChar { '.' }), level + 1)
            } ?: 0L.also { p("Null $r") }).also { cache[r] = it }
    }

    fun tryReduceL(r: R): R? = r.let { it.copy(damaged = it.damaged.trimStart { it == '.' }) }.let { rt ->
        when {
            rt.damaged.startsWith('?') -> rt
            rt.damaged.isNotEmpty() && rt.counts.isEmpty() -> null
            rt.counts.isNotEmpty() && rt.damaged.isEmpty() -> null
            rt.damaged.isEmpty() || rt.counts.isEmpty() -> rt
            else ->
                """^\.*(#+)([#?]*)\.*([#.?]*)""".toRegex().find(rt.damaged)
                    ?.takeIf { it.groupValues[1].length <= rt.counts.first() }
                    ?.takeIf { rt.counts.first() <= it.groupValues[1].length + it.groupValues[2].length }
                    ?.let { match ->
                        val matched = min(rt.counts.first(), match.groupValues[1].length + match.groupValues[2].length)
                        val remaining = rt.counts.first() - matched
                        val list = if (remaining == 0) emptyList() else listOf(remaining)
                        if (rt.damaged.drop(matched).firstOrNull() == '#') return null

                        tryReduceL(R(
                            rt.damaged.drop(matched + 1).trimStart { it == '.' },
                            (list + rt.counts.drop(1))
                        ))
                    }
        }
    }

    fun tryReduceR(r: R) = tryReduceL(R(r.damaged.reversed(), r.counts.reversed()))?.let {
        R(it.damaged.reversed(), it.counts.reversed())
    }

    fun tryReduce(r: R) = tryReduceL(r)?.let { tryReduceR(it) }

    fun par(lines: Sequence<String>, rate: Int = 1) = lines.map { line ->
        line.split(" ").let { (first, second) ->
            R(
                first.toList().multiply(rate, '?').joinToString(""),
                second.split(",").filter { it.isNotEmpty() }.map { it.toInt() }.multiply(rate)
            )
        }
    }

    fun <E> List<E>.multiply(times: Int, separator: E? = null) = 1.rangeTo(times).fold(emptyList<E>()) { acc, _ ->
        (if (separator == null) acc else acc + separator) + this
    }.drop(if (separator == null) 0 else 1)
}