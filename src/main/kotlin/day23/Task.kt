package day23

import java.io.File

typealias Path = List<P>

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = paths(parse(lines), P(0, 1)).maxOf { it.size }

    fun paths(board: List<List<Tile>>, p: P, last: P? = null): List<Path> =
        nextSteps(board, p, last)
            .flatMap { paths(board, it, p).map { path -> listOf(p) + path } }
            .takeIf { it.isNotEmpty() } ?: listOf(emptyList())

    private fun nextSteps(board: List<List<Tile>>, from: P, last: P?) = listOf(P.UP, P.DOWN, P.LEFT, P.RIGHT)
        .mapNotNull { move ->
            (from + move).takeIf { pos ->
                pos != last && pos.row in 0..board.lastIndex && pos.col in 0..board[0].lastIndex &&
                        when (board[pos.row][pos.col]) {
                            Tile.U -> move != P.DOWN
                            Tile.D -> move != P.UP
                            Tile.L -> move != P.RIGHT
                            Tile.R -> move != P.LEFT
                            Tile.F -> false
                            Tile.P -> true
                        }
            }
        }

    private fun parse(lines: Sequence<String>) = lines.map { it.map { c -> c.toTile() } }.toList()
}

data class P(val row: Int, val col: Int) {

    operator fun plus(delta: P) = P(row + delta.row, col + delta.col)

    companion object {
        val UP = P(-1, 0)
        val DOWN = P(1, 0)
        val LEFT = P(0, -1)
        val RIGHT = P(0, 1)
    }

    override fun toString() = "P($row, $col)"
}

private fun Char.toTile() = Tile.entries.first { it.c == this }

enum class Tile(val c: Char) {
    P('.'), F('#'), L('<'), R('>'), U('^'), D('v');
}