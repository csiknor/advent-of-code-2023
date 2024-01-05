package day19

import java.io.File
import kotlin.math.pow

object Task {
    /*
    The solution is pretty straightforward, as to parse the input, filter accepted parts based on the rules and sum
    their rating values. Acceptance a part means traversing through the rules matching the part's ratings results in an
    accepted state.
     */
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    /*
    In contrast with the original problem, part 2 works backwards. We only need the rules and can ignore the parts in
    the input. We filter the rules that result in acceptance and negate them so that their conditions define the ranges
    of the rating values that result in acceptance. For instance:
        * px{a<2006:qkq,m>2090:A,rfg} needs a>=2006 and m>2090
        * rfg{s<537:gd,x>2440:R,A} needs s>=537 and x<=2440
    Then we map the rules in reverse order to the negated rules to find all the conditions needed to be resulted in
    acceptance. Once we have it, we calculate the intersection of the ranges for all ratings, multiply them together and
    sum for all rules to get the final result.
     */
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    fun process(lines: Sequence<String>) = parse(lines).let { (rules, parts) ->
        parts.filter { accepted(rules, it) }.sumOf { it.rating.values.sum() }
    }

    private fun process2(lines: Sequence<String>) = parse(lines)
        .let { (rules, _) -> sumOfConditionRanges(acceptedRulesNegated(rules.values), reverse(rules.values)) }

    // We use the same iterator for the consecutive sequences to parse rules and parts
    fun parse(lines: Sequence<String>) = lines.iterator().let { linesIterator ->
        linesIterator.asSequence().takeWhile { it.isNotEmpty() }.mapNotNull { it.toRule() }.associateBy { it.name } to
                linesIterator.asSequence().map { it.toPart() }
    }

    private fun sumOfConditionRanges(acceptedRulesNegated: Sequence<Rule>, reversedRules: Map<String, List<Rule>>) =
        acceptedRulesNegated.sumOf { rule ->
            conditionRangesTo(rule, reversedRules)
                .values.map { it.last - it.first + 1L }
                .let { it.reduce { acc, value -> acc * value } * 4000.0.pow(4 - it.count()).toLong() }
        }

    private fun conditionRangesTo(rule: Rule, reversedRules: Map<String, List<Rule>>) = rulesTo(rule, reversedRules)
        .flatMap { it.conditions }
        .groupBy { it.cat }
        .mapValues { (_, conds) ->
            conds.groupBy { it.greater }.let { condsByType ->
                IntRange(
                    condsByType[true]?.maxOf { it.value + 1 } ?: 1,
                    condsByType[false]?.minOf { it.value - 1 } ?: 4000,
                )
            }
        }

    private fun rulesTo(rule: Rule, reversedRules: Map<String, List<Rule>>) =
        generateSequence(listOf(rule)) { revs -> revs.flatMap { reversedRules[it.name] ?: emptyList() } }
            .takeWhile { it.isNotEmpty() }
            .flatten()

    // Indexes rules with rule names that could result in the given rule
    private fun reverse(rules: Collection<Rule>) = rules
        .flatMap { r -> r.conditions.mapIndexed { i, c -> c.result to r.negate(i) } + (r.default to r.negate()) }
        .groupBy({ it.first }, { it.second })

    // Alternative version of rules that if all conditions are satisfied accepted result is expected
    private fun acceptedRulesNegated(rules: Collection<Rule>) =
        rules.asSequence().filter { rule -> rule.default == "A" || rule.conditions.any { it.result == "A" } }
            .flatMap { rule ->
                rule.conditions.withIndex().filter { it.value.result == "A" }.map { rule.negate(it.index) }
                    .let { if (rule.default == "A") it + listOf(rule.negate()) else it }
            }

    private fun accepted(rules: Map<String, Rule>, part: Part) =
        generateSequence("in") { name -> rules[name]?.eval(part) }.last() == "A"

}

data class Part(val rating: Map<Char, Int>)

fun String.toPart() = substring(1..<lastIndex)
    .split(",")
    .associate { r ->
        r.split("=")
            .let { (c, v) -> c[0] to v.toInt() }
    }
    .let { Part(it) }

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

fun String.toRule() = """(\w+)\{(.*)}""".toRegex().find(this)?.groupValues?.let { (_, name, rest) ->
    rest.split(",").let { conditions ->
        conditions.dropLast(1).mapNotNull { it.toCondition() } to conditions.last()
    }.let { (conditions, default) -> Rule(name, conditions, default) }
}

fun String.toCondition() =
    """(\w)([<>])(\d+):(\w+)""".toRegex().find(this)?.groupValues
        ?.let { (_, att, op, value, res) -> Condition(att[0], op == ">", value.toInt(), res) }
