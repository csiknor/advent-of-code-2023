package day21

import java.io.File

object Task {
    fun solvePart1(filename: String, steps: Int = 64) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, steps) }

    fun solvePart2(filename: String, steps: Int = 64) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, steps) }

    fun process(lines: Sequence<String>, steps: Int) = parse(lines).let { board ->
        dist(findStart(board), 0, steps, board, mutableMapOf())
            .also { dists -> printDistances(board, dists) }
            .filter { (_, count) -> steps % 2 == count % 2 }
            .count()
    }

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

    private fun dist(
        p: P,
        step: Int,
        max: Int,
        board: List<List<Tile>>,
        visited: MutableMap<P, Int>
    ): MutableMap<P, Int> =
        visited
            .apply {
                put(p, step)
                if (step != max) neighbours(p, board)
                    .filter { (r, c) -> board[r][c] != Tile.R }
                    .filter { (visited[it] ?: 1000) > step + 1 }
//                    .also { println("Visiting: $p[$step] -> $it") }
                    .onEach { dist(it, step + 1, max, board, visited) }
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