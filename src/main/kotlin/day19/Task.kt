package day19

import java.io.File
import kotlin.math.pow

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    fun process(lines: Sequence<String>) = parse(lines)
    fun process2(lines: Sequence<String>) = parse2(lines)

    private fun parse(lines: Sequence<String>) = lines
        .fold(Triple(emptyMap<String, Rule>(), false, 0)) { (rules, processed, count), line ->
            when {
                line.isEmpty() -> Triple(rules, true, count)
                !processed -> Triple(rules + ruleEntryOf(line), false, count)

                else -> Triple(
                    rules, true,
                    count + (partOf(line).takeIf { accepted(rules, it) }?.rating?.values?.sum() ?: 0)
                )
            }
        }.let { (_, _, count) -> count }

    private fun parse2(lines: Sequence<String>) = lines
        .takeWhile { it.isNotEmpty() }
        .fold(emptyMap<String, Rule>()) { rules, line -> rules + ruleEntryOf(line) }.let { rules ->
            rules.values.asSequence().filter { rule ->
                rule.default == "A" || rule.conditions.any { it.result == "A" }
            }.flatMap { rule ->
                rule.conditions.withIndex().filter { it.value.result == "A" }.map { it.index }.map { rule.negate(it) } +
                        if (rule.default == "A") listOf(rule.negate())
                        else emptyList()
            }
            .onEach { println("Negated: $it") }
            .map { rule ->
                val revMap = rules.values
                    .flatMap { r -> r.conditions.mapIndexed { i, c -> c.result to r.negate(i) } + (r.default to r.negate()) }
                    .groupBy({ it.first }, { it.second })

                generateSequence(listOf(rule)) { revs -> revs.flatMap { revMap[it.name] ?: emptyList() } }
                    .takeWhile { it.isNotEmpty() }
                    .flatten()
                    .flatMap { it.conditions }
                    .groupBy { it.cat }
                    .onEach { println(it) }
                    .mapValues { (_, conds) ->
                        conds.groupBy { it.greater }.let { condsByType ->
                            IntRange(
                                condsByType[true]?.maxOf { it.value + 1 } ?: 1,
                                condsByType[false]?.minOf { it.value - 1 } ?: 4000,
                            )
                        }
                    }
                    .onEach { println(it) }
                    .mapValues { it.value.last - it.value.first + 1L }
                    .onEach { println(it) }
                    .let {
                        it.values.reduce { acc, i -> acc * i } * 4000.0.pow(4 - it.count()).toLong()
                    }
                    .also { println(it) }
            }.sum()
        }

    fun accepted(rules: Map<String, Rule>, part: Part) =
        generateSequence("in") { name ->
            rules[name]?.eval(part)
        }.last() == "A"

    fun ruleEntryOf(line: String) =
        """(\w+)\{(.*)}""".toRegex().find(line)!!.groupValues.let { (_, name, rest) ->
            rest.split(",").let { conditions ->
                conditions.dropLast(1).map { input ->
                    """(\w)([<>])(\d+):(\w+)""".toRegex()
                        .find(input)!!.groupValues.let { (_, att, op, value, res) ->
                            Condition(att[0], op == ">", value.toInt(), res)
                        }
                } to conditions.last()
            }.let { (conditions, accepted) -> name to Rule(name, conditions, accepted) }
        }
            .also { println("Rule: ${it.second}") }

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

data class Rule(val name: String, val conditions: List<Condition>, val default: String) {

    fun eval(part: Part) = conditions.firstOrNull { it.matches(part.rating) }?.result ?: default

    fun negate(index: Int? = null) = copy(conditions = conditions.mapIndexed { i, c ->
        if (i < (index ?: conditions.size)) c.copy(greater = !c.greater, value = c.value + if (c.greater) 1 else -1)
        else c
    }.take((index ?: conditions.lastIndex) + 1))

    override fun toString() = "Rule($name: $conditions else $default)"
}

data class Condition(val cat: Char, val greater: Boolean, val value: Int, val result: String) {
    fun matches(rating: Map<Char, Int>) = rating.getValue(cat)
        .let { it > value && greater || it < value && !greater }

    override fun toString() = "Condition($cat ${if (greater) '>' else '<'} $value -> $result)"
}
