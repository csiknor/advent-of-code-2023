package day5

import java.io.File
import kotlin.math.max
import kotlin.math.min

data class MappingRange(val source: LongRange, val delta: Long)

typealias Mapping = List<MappingRange>

object Task2 {

    // Similarly to Task1, we parse the input seeds and mappings, although in this case, seeds are parsed into ranges,
    // instead of individual seeds. Once parsed, we execute the same logic as before, with one subtle but important
    // difference: we don't map each seed on by one, but rather as ranges. This is important, so that performance
    // remains good. When applying a mapping to a range of seeds, we split the range by the mapping ranges one-by-one
    // into three parts: 1) range part before mapping range, 2) range part intersecting with mapping range and 3) range
    // part after the mapping range. Then we apply the mapping delta to the intersection and do the same using the next
    // mapping range on the third range part until we run out of mapping ranges.
    fun solve(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = parseInput(lines)
        .let { (seedRanges, mappingLists) ->
            mappingLists.fold(seedRanges) { ranges, mapping ->
                ranges.flatMap { range -> splitAndApplyByMapping(mapping, range) }
            }.minOf { it.first }
        }
        .also { println("Min: $it") }

    // Splits the range according to mapping ranges and applies delta to intersections. The last range part from the
    // split of the previous mapping row is always the remaining range to be mapped.
    private fun splitAndApplyByMapping(mapping: Mapping, range: LongRange) =
        mapping.fold(listOf<LongRange?>(range)) { acc, (src, delta) ->
            acc.last()?.overlaps(src)?.let { (before, intersect, after) ->
                acc.dropLast(1).plus(listOf(before, intersect?.shift(delta), after))
            } ?: acc
        }.filterNotNull()

    private fun parseInput(lines: Sequence<String>) = lines
        .fold(listOf(emptyList<String>())) { sections, line ->
            when {
                line.isNotBlank() -> sections.plusElementToLast(line)
                else -> sections.plusEmptyElement()
            }
        }
        .let { sections -> seedsOf(sections.first().first()) to sections.drop(1).map { mappingOf(it) } }

    private fun seedsOf(input: String) = input.dropWhile { it != ' ' }.trim()
        .split(' ')
        .map { it.toLong() }
        .chunked(2) { (start, length) -> start..<start + length }
        .sortedBy { it.first }

    private fun mappingOf(section: List<String>) =
        section.drop(1)
            .map { line -> line.split(" ").map { it.toLong() } }
            .sortedBy { (_, src, _) -> src }
            .map { (dst, src, range) -> MappingRange(src..<src + range, dst - src) }
}

fun List<List<String>>.plusElementToLast(value: String) =
    dropLast(1).plusElement(last() + value)

fun List<List<String>>.plusEmptyElement() = plusElement(emptyList())

fun LongRange.shift(delta: Long) = (first + delta)..(last + delta)

// Calculates overlaps with another range: range parts before, intersecting and ofter the other range.
fun LongRange.overlaps(other: LongRange) =
    Triple(
        if (other.first <= first) null else first..min(last, other.first - 1),
        if (other.last < first || last < other.first) null else max(first, other.first)..min(last, other.last),
        if (last <= other.last) null else max(first, other.last + 1)..last
    )
