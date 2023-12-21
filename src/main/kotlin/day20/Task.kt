package day20

import day20.Pulse.HIGH
import day20.Pulse.LOW
import day8.lcm
import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    fun process(lines: Sequence<String>) = parse(lines).let { modules ->
        modules.forEach { (name, module) ->
            module.targets.mapNotNull { modules[it] }.onEach { it.register(name) }
        }
        modules.forEach { (_, module) -> println("Module: $module") }
        generateSequence(0 to 0) { (low, high) ->
            execute(modules).let { (pulseCounts, _) ->
                (low + (pulseCounts.getOrDefault(LOW, 0)) to
                        (high + pulseCounts.getOrDefault(HIGH, 0)))
            }
        }.take(1 + 1000).last()
            .let { (l, h) -> l * h }
    }

    private fun process2(lines: Sequence<String>) = parse(lines).let { modules ->
        modules.forEach { (name, module) ->
            module.targets.mapNotNull { modules[it] }.onEach { it.register(name) }
        }
        modules.forEach { (_, module) -> println("Module: $module") }

        numbersTillPulse(modules).fold(1L) { acc, i -> lcm(acc, i) }
    }

    private fun numbersTillPulse(modules: Map<String, Module>) = modules.getValue("broadcaster").targets.map {
        buildBinaryString(modules, binaryCounterGroup(modules, it)).toLong(2)
    }

    private fun buildBinaryString(modules: Map<String, Module>, names: List<String>) = names.joinToString("") {
        if (modules.getValue(it).targets.any { t -> modules.getValue(t) is Conjunction }) "1" else "0"
    }.reversed()

    private fun binaryCounterGroup(modules: Map<String, Module>, name: String): List<String> =
        listOf(name) + modules.getValue(name).targets
            .map { modules.getValue(it) }
            .filterIsInstance<FlipFlop>()
            .flatMap { binaryCounterGroup(modules, it.name) }

    private fun execute(modules: Map<String, Module>) =
        generateSequence(listOf(Signal("button", "broadcaster", LOW)) to 0) { (signals, index) ->
            if (index < signals.size)
                Pair(
                    signals + signals[index]//.also { println(it) }
                        .let { modules[it.target]?.process(it.source, it.pulse) ?: emptyList() },
                    (index + 1)
                )
            else null
        }.last().let { (signals, _) ->
            signals.groupingBy { it.pulse }.eachCount() to
                    signals.any { it.pulse == LOW && it.target == "rx" }
        }
    //.also { println(it) }

    fun parse(lines: Sequence<String>) = lines.map { line ->
        line.split(" -> ").let { (source, targetInput) ->
            targetInput.split(", ").let { targets ->
                when {
                    source == "broadcaster" -> Broadcaster(targets)
                    source.startsWith('%') -> FlipFlop(source.substring(1), targets)
                    source.startsWith('&') -> Conjunction(source.substring(1), targets)
                    else -> throw IllegalArgumentException("Unknown module: $source")
                }//.also { println("Created: $it") }
            }
        }
    }.associateBy { it.name }
}

enum class Pulse { HIGH, LOW }

data class Signal(val source: String, val target: String, val pulse: Pulse) {
    override fun toString() = "Signal($source —$pulse-> $target)"
}

abstract class Module(
    val name: String,
    val targets: List<String>,
    private val sources: MutableList<String> = mutableListOf()
) {

    abstract fun process(source: String, pulse: Pulse): List<Signal>

    open fun register(source: String) {
        sources.add(source)
    }
}

class Broadcaster(targets: List<String>) : Module("broadcaster", targets) {

    override fun process(source: String, pulse: Pulse) = targets.map { Signal(name, it, pulse) }

    override fun toString() = "Broadcaster($targets)"
}

class FlipFlop(name: String, targets: List<String>, private var on: Boolean = false) : Module(name, targets) {

    override fun process(source: String, pulse: Pulse) = when (pulse) {
        HIGH -> emptyList()
        LOW -> flip().let { flipped ->
            targets.map { Signal(name, it, if (flipped) HIGH else LOW) }
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
        return targets.map { target -> Signal(name, target, if (pulses.all { it.value == HIGH }) LOW else HIGH) }
    }

    private fun store(source: String, pulse: Pulse) {
        pulses[source] = pulse
    }

    override fun register(source: String) {
        super.register(source)
        pulses[source] = LOW
    }

    override fun toString() = "$name = Conjunction($pulses, $targets)"
}