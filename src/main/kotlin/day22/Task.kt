package day22

import java.io.File
import java.util.*

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    fun process2(lines: Sequence<String>) = fallen(parse(lines)).let { all ->
        all.sumOf { pick ->
            (all - pick)
                .runningFold(emptyList<Brick>() to 0) { (acc, count), brick ->
                    acc.filter { it intersects brick }.maxOfOrNull { it.b.z + 1 }
                        .let { acc + brick.atZ(it ?: 1) to (count + (if (brick.a.z != (it ?: 1)) 1 else 0)) }
                }.last().second
        }
    }

    fun process(lines: Sequence<String>) = fallen(parse(lines))
        .groupByTo(TreeMap<Int, MutableList<Brick>>(compareBy<Int> { it })) { it.a.z }
        .onEach { println("${it.key} -> ${it.value}") }
        .values

        .fold(emptySet<Brick>() to emptySet<Brick>()) { (acc, required), bricks ->
            (acc + bricks) to (required + requiredSupport(acc, bricks.toSet()))
        }
        .let { (bricks, required) -> bricks - required }.size

    private fun requiredSupport(acc: Set<Brick>, bricks: Set<Brick>) =
        if (acc.isEmpty()) emptySet()
        else bricks.map { brick -> supporting(acc, brick) }
            .filter { it.size == 1 }
            .reduceOrNull { a, b -> a.union(b) }
            ?: emptySet()

    private fun supporting(acc: Set<Brick>, brick: Brick) =
        acc
            .filter { brick.a.z == it.b.z + 1 && brick intersects it }
            .toSet()

    private fun fallen(bricks: Sequence<Brick>) = bricks
        .runningFold(emptyList<Brick>()) { acc, brick ->
            acc + acc.filter { a -> a intersects brick }.maxOfOrNull { it.b.z + 1 }.let { brick.atZ(it ?: 1) }
        }.last()

    fun minByX(brick: Brick, acc: List<Brick>) =
        acc.filter { b -> b.a.x..b.b.x intersects brick.a.x..brick.b.x }
            .maxOfOrNull { it.b.z + 1 } ?: 1


    fun minByY(brick: Brick, acc: List<Brick>) =
        acc.filter { b -> b.a.y..b.b.y intersects brick.a.y..brick.b.y }
            .maxOfOrNull { it.b.z + 1 } ?: 1

    fun parse(lines: Sequence<String>) = lines.map { it.toBrick() }
        .sortedBy { it.a.z }
}

infix fun Brick.intersects(other: Brick) =
    a.x..b.x intersects other.a.x..other.b.x &&
            a.y..b.y intersects other.a.y..other.b.y

infix fun IntRange.intersects(r2: IntRange) =
    first in r2 || last in r2 || r2.first in this || r2.last in this

private fun Brick.atZ(z: Int) =
    if (a.z == z) this
    else Brick(a.copy(z = z), b.copy(z = z + b.z - a.z)).also { check(a.z >= z) { "$this —/-> $z" } }

data class P(val x: Int, val y: Int, val z: Int) {
    override fun toString() = "$x,$y,$z"
}

fun String.toP() = split(",").map { it.toInt() }.let { (x, y, z) -> P(x, y, z) }

data class Brick(val a: P, val b: P) {

    override fun toString() = "$a~${size()}"

    private fun size() = when {
        a.x != b.x -> "${b.x - a.x + 1}w"
        a.y != b.y -> "${b.y - a.y + 1}d"
        a.z != b.z -> "${b.z - a.z + 1}t"
        else -> "1c"
    }
}

fun String.toBrick() = split("~")
    .map { it.toP() }
    .let { (a, b) -> Brick(a, b).also { check(a.x <= b.x && a.y <= b.y && a.z <= b.z) } }