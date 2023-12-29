package day12

import java.io.File
import kotlin.math.min

object Task {

    /*
    We parse the input into rows of conditions and a list of damage count groups. For each row we try to reduce the
    input so that only the unknown part remains. Then we calculate the possible number of arrangements of unknown
    damages by trying both operational and damaged options.
     */
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    /*
    Given the bigger rate the input is much larger, and a cache is introduced to reduce the computation of already
    known parts of the input.
     */
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, 5) }

    fun process(lines: Sequence<String>, rate: Int = 1) = parse(lines, rate).sumOf { arrangeCount(it) }

    /*
    We try to reduce the input, and if there's still unknown conditions then we try both operational and damaged options
    and recursively count the arrangements on both. If we couldn't reduce the input then it was an incorrect assumption
    of the unknown condition and we ignore.
     */
    fun arrangeCount(row: Row, cache: MutableMap<Row, Long> = mutableMapOf()): Long = cache.getOrPut(row) {
        if (row.conditions.isEmpty()) 1L else
            tryReduce(row)?.let {
                if (it.conditions.isEmpty()) 1L
                else
                    arrangeCount(it.copy(conditions = it.conditions.replaceFirstChar { '#' }), cache) +
                            arrangeCount(it.copy(conditions = it.conditions.replaceFirstChar { '.' }), cache)
            } ?: 0L
    }

    /*
    Reducing consists of trimming operational springs at the beginning. Then, if the first remaining spring is unknown
    or no springs left, we're done. However, if the conditions and the number of damaged groups are inconsistent, we
    hit an incorrect input.
    Otherwise, we try to find the possible damaged wells byt looking at damaged springs followed by unknown conditions
    and comparing the count of them to the first group of damaged count. If they match we drop the matched amount and
    re-reduce.
     */
    fun tryReduceL(row: Row): Row? = row.let { it.copy(conditions = it.conditions.trimStartPeriod()) }.let {
        when {
            it.conditions.startsWith('?') || it.isEmpty() -> it
            it.isInconsistent() -> null
            else ->
                findDamagesAndUnknowns(it.conditions)
                    ?.takeIf { (damages) -> damages.length <= it.counts.first() }
                    ?.takeIf { (damages, unknowns) -> it.counts.first() <= damages.length + unknowns.length }
                    ?.let { (damages, unknowns) -> min(it.counts.first(), damages.length + unknowns.length) }
                    ?.takeUnless { matched -> it.conditions.drop(matched).firstOrNull() == '#' }
                    ?.let { matched -> tryReduceL(it.dropFirst(matched)) }
        }
    }

    private fun Row.dropFirst(matched: Int) = Row(
        conditions.drop(matched + 1).trimStartPeriod(),
        (listOf(counts.first() - matched).filter { it != 0 } + counts.drop(1))
    )

    private fun String.trimStartPeriod() = trimStart { it == '.' }

    private fun findDamagesAndUnknowns(input: String) =
        """^(#+)([#?]*)\.*([#.?]*)""".toRegex().find(input)?.groupValues?.subList(1, 3)

    private fun Row.isEmpty() = conditions.isEmpty() && counts.isEmpty()

    private fun Row.isInconsistent() =
        conditions.isNotEmpty() && counts.isEmpty() || counts.isNotEmpty() && conditions.isEmpty()

    // Reducing from the right is the same as reducing the reversed row from the left.
    fun tryReduceR(row: Row) = tryReduceL(row.reversed())?.reversed()

    private fun Row.reversed() = Row(conditions.reversed(), counts.reversed())

    // Reducing is attempted both from left and right
    fun tryReduce(row: Row) = tryReduceL(row)?.let { tryReduceR(it) }

    fun parse(lines: Sequence<String>, rate: Int = 1) =
        lines.map { line ->
            line.split(" ").let { (first, second) ->
                Row(
                    List(rate) { first }.joinToString("?"),
                    second.split(",").filter { it.isNotEmpty() }.map { it.toInt() } * rate
                )
            }
        }

    operator fun <E> List<E>.times(times: Int) = List(times) { this }.flatten()
}

data class Row(val conditions: String, val counts: List<Int>)
