package day5

import java.io.File

object Task1 {

    // We parse the input seeds and mappings. In case of mappings the names are not important, we just store them as a
    // list and going to use them in their default order. Then, for each seed we traverse the mappings and fold the
    // mapped values into its final fully mapped value, which is the location. With that we calculate the minimum.
    fun solve(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines
        .fold(mutableListOf(mutableListOf<String>())) { sections, line ->
            if (line.isNotBlank()) {
                sections.last().add(line)
            } else {
                sections.add(mutableListOf())
            }
            sections
        }
        .fold(emptyList<Long>() to emptyList<Map<LongRange, Long>>()) { (seeds, sections), section ->
            """seeds: (.*)""".toRegex().find(section.first())
                ?.let { it.groupValues[1].split("""\s+""".toRegex()).map { it.toLong() } to sections }
                ?: (seeds to sections + sectionOf(section))
        }
        .let { (seeds, mappings) ->
            seeds.minOf { seed ->
                mappings.fold(seed) { curr, map ->
                    map.firstNotNullOfOrNull { (key, value) -> value.takeIf { key.contains(curr) } }
                        ?.let { curr + it } ?: curr
                }
            }
        }
        .also { println("Min: $it") }

    // Parses the input into a map of range and value, where range is the source range and the value is the delta
    // between the source and the destination range. This way we can map a source value to a dest value by looking up
    // the range in the keys that contains the source value and calculate the destination value using the stored delta.
    private fun sectionOf(section: List<String>) =
        section.drop(1)
            .fold(mapOf<LongRange, Long>()) { acc, s ->
                acc + s.split(" ")
                    .map { it.toLong() }
                    .let { (dst, src, range) ->
                        src.rangeTo(src + range) to (dst - src)
                    }
            }

}