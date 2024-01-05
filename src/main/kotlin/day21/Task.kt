package day21

import java.io.File
import java.util.*

object Task {

    /*
    The solution is based on parsing the input into a board of tiles, where we map each possible step until the maximum
    number of steps or the edges of the board using a breadth first algorithm. Then the reachable tiles are those that
    even.
     */
    fun solvePart1(filename: String, steps: Int = 64) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, steps) }

    /*
    The solution of part 2 relies on some properties of the input instead of being a general solution, which would be
    very slow. Instead, we realise that the repeating boards have alternating traits of even and odd step counts. Also,
    since the start is in the middle and there are no rocks blocking the way horizontally or vertically, the ultimate
    tiles can be reached is diamond shaped. Using these two we can make assumptions about how many boards will we be
    able to be reach in 26,501,365 steps, which is exactly 202,300 boards on top of the starting board.
    See more detail in and kudos to the author of the [solution](https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21).
     */
    fun solvePart2(filename: String, steps: Int = 26501365) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line, steps) }

    // Not a generic solution, builds on the properties of the input
    private fun process2(lines: Sequence<String>, steps: Int) = parse(lines)
        .let { board -> extrapolate(reachableBoardsCount(steps, board), dist(findStart(board), steps, board), board) }

    private fun reachableBoardsCount(steps: Int, board: List<List<Tile>>) = (steps - (board.size / 2L)) / board.size

    // Extrapolate the reachable tile count using the traits of the input
    private fun extrapolate(n: Long, dists: MutableMap<P, Int>, board: List<List<Tile>>) =
        ((n + 1) * (n + 1)) * oddFull(dists) + (n * n) * evenFull(dists) -
                (n + 1) * oddCorners(dists, board) + n * evenCorners(dists, board)

    private fun oddFull(dists: MutableMap<P, Int>) = dists.values.count { it % 2 == 1 }

    private fun evenFull(dists: MutableMap<P, Int>) = dists.values.count { it % 2 == 0 }

    private fun oddCorners(dists: MutableMap<P, Int>, board: List<List<Tile>>) =
        dists.values.count { it % 2 == 1 && it > board.size / 2L }

    private fun evenCorners(dists: MutableMap<P, Int>, board: List<List<Tile>>) =
        dists.values.count { it % 2 == 0 && it > board.size / 2L }

    fun process(lines: Sequence<String>, steps: Int) = parse(lines).let { board ->
        dist(findStart(board), steps, board)
            .also { printDistances(board, it) }
            .filter { (_, count) -> steps % 2 == count % 2 }
            .count()
    }

    private fun dist(start: P, max: Int, board: List<List<Tile>>) =
        generateSequence(LinkedList(listOf(start to 0)) to mutableMapOf(start to 0)) { (queue, seen) ->
            queue.poll()
                .takeIf { (_, step) -> step != max }
                ?.let { (p, step) ->
                    nextSteps(p, board, seen, step)
                        .let { queue.apply { addAll(it) } to seen.apply { putAll(it) } }
                }
                ?: (queue to seen)
        }
            .takeWhile { (queue, _) -> queue.isNotEmpty() }
            .last().let { (_, seen) -> seen }

    private fun nextSteps(p: P, board: List<List<Tile>>, seen: MutableMap<P, Int>, step: Int) =
        neighbours(p, board)
            .filter { (r, c) -> board[r][c] != Tile.R }
            .filter { it !in seen }
            .map { it to (step + 1) }

    private fun printDistances(board: List<List<Tile>>, dists: MutableMap<P, Int>) {
        board.forEachIndexed { r, tiles ->
            tiles.mapIndexed { c, tile ->
                when (tile) {
                    Tile.R -> "*"
                    else -> dists[P(r, c)]?.toString() ?: "."
                }.padStart(2)
            }.joinToString(" ").also { println(it) }
        }.also { println() }
    }

    private fun neighbours(pos: P, board: List<List<Tile>>) =
        setOf(P.UP, P.DOWN, P.LEFT, P.RIGHT).mapNotNull { delta ->
            (pos + delta).takeIf { p -> p.row in board.indices && p.col in board[0].indices }
        }

    private fun findStart(board: List<List<Tile>>) = board.indexOfFirst { row -> row.contains(Tile.S) }
        .let { i -> P(i, board[i].indexOfFirst { c -> c == Tile.S }) }

    private fun parse(lines: Sequence<String>) = lines.map { line -> line.map { it.toTile() } }.toList()
}

data class P(val row: Int, val col: Int) {

    operator fun plus(delta: P) = P(row + delta.row, col + delta.col)

    companion object {
        val UP = P(-1, 0)
        val DOWN = P(1, 0)
        val LEFT = P(0, -1)
        val RIGHT = P(0, 1)
    }
}

enum class Tile { G, R, S }

fun Char.toTile() = when (this) {
    '.' -> Tile.G
    '#' -> Tile.R
    'S' -> Tile.S
    else -> error("Unknown tile: $this")
}