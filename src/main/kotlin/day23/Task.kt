package day23

import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, false) }

    fun process(lines: Sequence<String>, slippery: Boolean = true) =
        pathSize(parse(lines), P(0, 1), slippery = slippery) ?: 0

    fun pathSize(board: MutableList<MutableList<Tile>>, p: P, slippery: Boolean): Int? =
        if (p.row == board.lastIndex) board.sumOf { row -> row.count { it == Tile.O } }
        else
            board[p.row][p.col].let { orig ->
                nextSteps(board, p, slippery)
                    .also {board[p.row][p.col] = Tile.O }
                    .mapNotNull { pathSize(board, it, slippery) }
                    .maxOrNull()
                    .also { board[p.row][p.col] = orig }
            }

    private fun nextSteps(board: List<List<Tile>>, from: P, slippery: Boolean) =
        listOf(P.UP, P.DOWN, P.LEFT, P.RIGHT)
            .mapNotNull { move ->
                (from + move).takeIf { pos ->
                    pos.row in board.indices && pos.col in board[0].indices &&
                            when (board[pos.row][pos.col]) {
                                Tile.U -> !slippery || move != P.DOWN
                                Tile.D -> !slippery || move != P.UP
                                Tile.L -> !slippery || move != P.RIGHT
                                Tile.R -> !slippery || move != P.LEFT
                                Tile.P -> true
                                Tile.F, Tile.O -> false
                            }
                }
            }

    private fun parse(lines: Sequence<String>) = lines.map { it.mapTo(mutableListOf()) { c -> c.toTile() } }.toMutableList()
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
    P('.'), F('#'), L('<'), R('>'), U('^'), D('v'), O('O')
}