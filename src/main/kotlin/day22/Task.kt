package day22

import java.io.File
import java.util.*

object Task {

    /*
    The solution is parsing the input into bricks and then letting them fall so that supporting bricks can be
    calculated. When a brick is supported by only one brick then that one is required, everything else can be safely
    removed without a brick falling down. Falling and supporting relies on the same logic: if there's an intersecting
    brick below. This can be figured byt looking at the horizontal plane (x, y) intersection of bricks and their
    elevation (z).
     */
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    /*
    For part 2 we need to try to remove bricks one by one and see how many bricks would fall as a consequence.
    Note: this is not too fast, but performs well enough.
     */
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    fun process2(lines: Sequence<String>) = fallen(parse(lines)).first
        .let { all -> all.sumOf { pick -> fallen(all.asSequence().minus(pick)).second } }

    fun process(lines: Sequence<String>) = fallen(parse(lines)).first
        .groupByTo(TreeMap<Int, MutableList<Brick>>(compareBy<Int> { it })) { it.a.z }
        .values.fold(mutableSetOf<Brick>() to mutableSetOf<Brick>()) { (acc, required), bricks ->
            acc.apply { addAll(bricks) } to required.apply { addAll(requiredSupport(acc, bricks.toSet())) }
        }.let { (bricks, required) -> bricks - required }.size

    private fun requiredSupport(acc: Set<Brick>, bricks: Set<Brick>) =
        if (acc.isEmpty()) emptySet()
        else bricks.asSequence()
            .map { supporting(acc, it) }
            .filter { it.size == 1 }
            .reduceOrNull { a, b -> a.union(b) }
            ?: emptySet()

    private fun supporting(acc: Set<Brick>, brick: Brick) =
        acc.asSequence()
            .filter { brick.a.z == it.b.z + 1 && brick intersects it }
            .toSet()

    private fun fallen(bricks: Sequence<Brick>): Pair<List<Brick>, Int> = bricks
        .runningFold(mutableListOf<Brick>() to 0) { (acc, count), brick ->
            acc.filter { it intersects brick }.maxOfOrNull { it.b.z + 1 }
                .let {
                    acc.apply { add(brick.atZ(it ?: 1)) } to if (brick.a.z != (it ?: 1)) count + 1 else count
                }
        }.last()

    fun parse(lines: Sequence<String>) = lines.map { it.toBrick() }.sortedBy { it.a.z }
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