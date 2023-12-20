package day20

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = parse(lines).let { modules ->
        modules.forEach { (name, module) ->
            module.targets.mapNotNull { modules[it] }.onEach { it.register(name) }
        }
        modules.forEach { (_, module) -> println("Module: $module") }
        generateSequence(0 to 0) { (low, high) ->
            execute(modules).let { (l, h) -> (low + l) to (high + h) }
        }.take(1+1000).last()
            .let { (l,h) -> l*h }
    }

    private fun execute(modules: Map<String, Module>) =
        generateSequence(listOf(Signal("button", "broadcaster", Pulse.LOW)) to 0) { (signals, index) ->
            if (index < signals.size)
                Pair(
                    signals + signals[index].also { println(it) }
                        .let { modules[it.target]?.process(it.source, it.pulse) ?: emptyList() },
                    (index + 1)
                )
            else null
        }.last().first.groupingBy { it.pulse }.eachCount()
            .also { println(it) }
            .let { pulseCounts ->
                (pulseCounts[Pulse.LOW] ?: 0) to (pulseCounts[Pulse.HIGH] ?: 0)
//                pulseCounts.values.reduce { acc, i -> acc * i }
            }

    fun parse(lines: Sequence<String>) = lines.map { line ->
        line.split(" -> ").let { (source, targetInput) ->
            targetInput.split(", ").let { targets ->
                when {
                    source == "broadcaster" -> Broadcaster(targets)
                    source.startsWith('%') -> FlipFlop(source.substring(1), targets)
                    source.startsWith('&') -> Conjunction(source.substring(1), targets)
                    else -> throw IllegalArgumentException("Unknown module: $source")
                }.also { println("Created: $it") }
            }
        }
    }.associateBy { it.name }
}

enum class Pulse { HIGH, LOW }

data class Signal(val source: String, val target: String, val pulse: Pulse) {
    override fun toString() = "Signal($source —$pulse-> $target)"
}

abstract class Module(val name: String, val targets: List<String>) {

    abstract fun process(source: String, pulse: Pulse): List<Signal>

    open fun register(source: String) {}
}

class Broadcaster(targets: List<String>) : Module("broadcaster", targets) {

    override fun process(source: String, pulse: Pulse) = targets.map { Signal(name, it, pulse) }

    override fun toString() = "Broadcaster($targets)"
}

class FlipFlop(name: String, targets: List<String>, private var on: Boolean = false) : Module(name, targets) {

    override fun process(source: String, pulse: Pulse) = when (pulse) {
        Pulse.HIGH -> emptyList()
        Pulse.LOW -> flip().let { flipped ->
            targets.map { Signal(name, it, if (flipped) Pulse.HIGH else Pulse.LOW) }
        }
    }

    private fun flip(): Boolean {
        on = !on
        return on
    }

    override fun toString() = "$name = FlipFlow($on, $targets)"
}

class Conjunction(
    name: String,
    targets: List<String>,
    private val pulses: MutableMap<String, Pulse> = mutableMapOf()
) : Module(name, targets) {

    override fun process(source: String, pulse: Pulse): List<Signal> {
        store(source, pulse)
        return targets.map { Signal(name, it, if (pulses.all { it.value == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH) }
    }

    private fun store(source: String, pulse: Pulse) {
        pulses[source] = pulse
    }

    override fun register(source: String) {
        pulses[source] = Pulse.LOW
    }

    override fun toString() = "$name = Conjunction($pulses, $targets)"
}