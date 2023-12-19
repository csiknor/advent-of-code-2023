package day19

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = parse(lines).also { println(it) }

    private fun parse(lines: Sequence<String>) = lines
        .fold(Triple(emptyMap<String, Rule>(), false, 0)) { (rules, processed, count), line ->
            when {
                line.isEmpty() -> Triple(rules, true, count)

                !processed -> Triple(
                    rules + ruleEntryOf(line),
                    false,
                    count
                )

                else -> Triple(rules, true, count + (partOf(line).takeIf { accepted(rules, it).also { println("=> $it") } }?.rating?.values?.sum() ?: 0))
            }
        }.let { (_, _, count) -> count }

    fun accepted(rules: Map<String, Rule>, part: Part) =
        generateSequence("in") { name ->
            rules[name]?.eval(part)
        }
            .onEach { println("Mapping: $it") }
            .last().also { println("Last: $it") } == "A"

    fun ruleEntryOf(line: String) =
        """(\w+)\{(.*)}""".toRegex().find(line)!!.groupValues.let { (_, name, rest) ->
            rest.split(",").let { conditions ->
                conditions.dropLast(1).map { input ->
                    """(\w)([<>])(\d+):(\w+)""".toRegex()
                        .find(input)!!.groupValues.let { (_, att, op, value, res) ->
                            Condition(att[0], op == ">", value.toInt(), res)
                        }
                } to conditions.last()
            }.let { (conditions, accepted) -> name to Rule(conditions, accepted) }
        }
            .also { println("Rule: $it") }

    fun partOf(line: String) =
        line.substring(1..<line.lastIndex)
            .split(",")
            .associate { r ->
                r.split("=")
                    .let { (c, v) -> c[0] to v.toInt() }
            }
            .let { Part(it) }
            .also { println("Part: $it") }

}

data class Part(val rating: Map<Char, Int>)

data class Rule(val conditions: List<Condition>, val default: String) {
    fun eval(part: Part) = conditions.firstOrNull { it.matches(part) }?.result ?: default
}

data class Condition(val cat: Char, val greater: Boolean, val value: Int, val result: String) {
    fun matches(part: Part) = part.rating.getValue(cat)
        .also { print("matches: $it != $value && ($it < $value || $greater)") }
        .let { rating -> rating > value && greater || rating < value && !greater }
        .also { println(" -> $it") }
}
