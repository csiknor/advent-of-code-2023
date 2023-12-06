package day5

import java.io.File

object Task2 {

    // Similarly to Task1, we parse the intput int seeds and mappings, although in this case, seeds are parsed into
    // ranges, instead of individual seeds. Once parsed, we execute the same logic as before.
    // WARNING: this is a brute force algorithm, and as such very slow.
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
        .fold(emptyList<LongRange>() to emptyList<Map<LongRange, Long>>()) { (seeds, sections), section ->
            """seeds: (.*)""".toRegex().find(section.first())
                ?.let {
                    it.groupValues[1].split("""\s+""".toRegex())
                        .map { it.toLong() }
                        .chunked(2) { (start, length) ->
                            start..<start + length
                        } to sections
                }
                ?: (seeds to sections + sectionOf(section))
        }
        .let { (seeds, mappings) ->
            seeds.minOf { seedRange ->
                seedRange.minOf { seed ->
                    mappings.fold(seed) { curr, map ->
                        map.firstNotNullOfOrNull { (key, value) -> value.takeIf { key.contains(curr) } }
                            ?.let { curr + it }
                            ?: curr
                    }
                }
            }
        }
        .also { println("Min: $it") }

    private fun sectionOf(section: List<String>) =
        section.drop(1)
            .fold(mapOf<LongRange, Long>()) { acc, s ->
                acc + s.split(" ")
                    .map { it.toLong() }
                    .let { (dst, src, range) ->
                        src..<src + range to (dst - src)
                    }
            }

}