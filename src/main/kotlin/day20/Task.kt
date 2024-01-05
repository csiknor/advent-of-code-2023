package day20

import day20.Pulse.HIGH
import day20.Pulse.LOW
import day8.lcm
import java.io.File

object Task {
    /*
    The solution is to parse the input into the appropriate types of modules which then used to process the signals
    emitted by pressing the button a thousand times, followed by counting the low and high signals and multiplying the
    sum to get the final result.
     */
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    /*
    The second part was beyond me and just ported a [solution](https://github.com/ash42/adventofcode/blob/main/adventofcode2023/src/nl/michielgraat/adventofcode2023/day20/Day20.java#L159)
    that involved fabricating a binary number of based on flip-flop and conjunction modules in a row. Works like a
    charm, kudos to the author.
     */
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    fun process(lines: Sequence<String>) = pulseCountsAfterThousandButtonPress(registerSources(parse(lines)))
        .let { (l, h) -> l * h }

    private fun process2(lines: Sequence<String>) =
        numbersTillPulse(registerSources(parse(lines))).fold(1L) { acc, i -> lcm(acc, i) }

    private fun registerSources(modules: Map<String, Module>) = modules.onEach { (name, module) ->
        module.targets.mapNotNull { modules[it] }.onEach { it.register(name) }
    }

    private fun pulseCountsAfterThousandButtonPress(modules: Map<String, Module>) =
        generateSequence {
            pulseCountsOnButtonPress(modules).let { pulseCounts -> (pulseCounts[LOW] ?: 0) to (pulseCounts[HIGH] ?: 0) }
        }
            .take(1000).unzip().let { (lows, highs) -> lows.sum() to highs.sum() }

    private fun numbersTillPulse(modules: Map<String, Module>) =
        modules.getValue("broadcaster")
            .targets.map { buildBinaryString(modules, binaryCounterGroup(modules, it)).toLong(2) }

    private fun buildBinaryString(modules: Map<String, Module>, names: List<String>) =
        names.joinToString("") {
            if (modules.getValue(it).targets.any { t -> modules.getValue(t) is Conjunction }) "1" else "0"
        }.reversed()

    private fun binaryCounterGroup(modules: Map<String, Module>, name: String): List<String> =
        mutableListOf(name).apply {
            addAll(modules.getValue(name).targets
                .map { modules.getValue(it) }
                .filterIsInstance<FlipFlop>()
                .flatMap { binaryCounterGroup(modules, it.name) })
        }

    private fun pulseCountsOnButtonPress(modules: Map<String, Module>) =
        generateSequence(mutableListOf(Signal("button", "broadcaster", LOW)) to 0) { (signals, index) ->
            if (index == signals.size) null
            else (signals[index].process(modules)?.let { signals.apply { addAll(it) } } ?: signals) to (index + 1)
        }.last().first.groupingBy { it.pulse }.eachCount()

    fun Signal.process(modules: Map<String, Module>) = modules[target]?.process(source, pulse)

    fun parse(lines: Sequence<String>) = lines.map { line ->
        line.split(" -> ").let { (source, targetInput) -> moduleOf(source, targetInput.split(", ")) }
    }.associateBy { it.name }

    private fun moduleOf(source: String, targets: List<String>) = when {
        source == "broadcaster" -> Broadcaster(targets)
        source.startsWith('%') -> FlipFlop(source.substring(1), targets)
        source.startsWith('&') -> Conjunction(source.substring(1), targets)
        else -> error("Unknown module: $source")
    }
}

enum class Pulse { HIGH, LOW }

data class Signal(val source: String, val target: String, val pulse: Pulse) {
    override fun toString() = "Signal($source —$pulse-> $target)"
}

abstract class Module(val name: String, val targets: List<String>) {
    private val sources: MutableList<String> = mutableListOf()

    abstract fun process(source: String, pulse: Pulse): List<Signal>

    open fun register(source: String) {
        sources.add(source)
    }
}

class Broadcaster(targets: List<String>) : Module("broadcaster", targets) {

    override fun process(source: String, pulse: Pulse) = targets.map { Signal(name, it, pulse) }

    override fun toString() = "Broadcaster($targets)"
}

class FlipFlop(name: String, targets: List<String>) : Module(name, targets) {
    private var on: Boolean = false

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

class Conjunction(name: String, targets: List<String>) : Module(name, targets) {
    private val pulses: MutableMap<String, Pulse> = mutableMapOf()

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