package day21

import java.io.File
import java.util.*

object Task {
    fun solvePart1(filename: String, steps: Int = 64) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, steps) }

    fun solvePart2(filename: String, steps: Int = 26501365) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line, steps) }

    // Not a generic solution, builds on the properties of the input
    private fun process2(lines: Sequence<String>, steps: Int) = parse(lines).let { board ->
        dist(findStart(board), steps, board).let { dists ->
            val n = (steps - (board.size / 2L)) / board.size

            ((n + 1) * (n + 1)) * oddFull(dists) + (n * n) * evenFull(dists) -
                    (n + 1) * oddCorners(dists, board) + n * evenCorners(dists, board)
        }
    }

    private fun oddFull(dists: MutableMap<P, Int>) = dists.values.count { it % 2 == 1 }

    private fun evenFull(dists: MutableMap<P, Int>) = dists.values.count { it % 2 == 0 }

    private fun oddCorners(
        dists: MutableMap<P, Int>,
        board: List<List<Tile>>
    ) = dists.values.count { it % 2 == 1 && it > board.size / 2L }

    private fun evenCorners(
        dists: MutableMap<P, Int>,
        board: List<List<Tile>>
    ) = dists.values.count { it % 2 == 0 && it > board.size / 2L }

    fun process(lines: Sequence<String>, steps: Int) = parse(lines).let { board ->
        dist(findStart(board), steps, board)
            .also { printDistances(board, it) }
            .filter { (_, count) -> steps % 2 == count % 2 }
            .count()
    }

    private fun dist(start: P, max: Int, board: List<List<Tile>>): MutableMap<P, Int> =
        generateSequence(LinkedList(listOf(start to 0)) to mutableMapOf(start to 0)) { (queue, seen) ->
            val (p, step) = queue.poll()

            if (step != max)
                neighbours(p, board)
                    .filter { (r, c) -> board[r][c] != Tile.R }
                    .filter { it !in seen }
                    .map { it to (step + 1) }
                    .also {
                        seen.putAll(it)
                        queue.addAll(it)
                    }
            queue to seen
        }.takeWhile { (queue, _) -> queue.isNotEmpty() }
            .last().let { (_, seen) -> seen }

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

    private fun parse(lines: Sequence<String>) = lines.map { line ->
        line.map { c ->
            when (c) {
                '.' -> Tile.G
                '#' -> Tile.R
                'S' -> Tile.S
                else -> throw IllegalArgumentException("Unknown tile: $c")
            }
        }.toList()
    }.toList()

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