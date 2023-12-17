package day17

import java.io.File
import java.util.PriorityQueue

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line, 4, 10) }

    fun process(lines: Sequence<String>, least: Int = 1, most: Int = 3) = parse(lines).let { graph ->
        minHeat(graph, P(0, 0), P(graph.lastIndex, graph[0].lastIndex), least, most)
    }

    fun minHeat(board: List<List<Int>>, start: P, end: P, least: Int, most: Int): Int {
        val queue = PriorityQueue(
            compareBy<Pair<Int, Move>> { it.first }.thenBy { it.second.point }.thenBy { it.second.delta })
        queue.add(0 to Move(start, P(0, 0)))
        val seen = mutableSetOf<Move>()

        while (queue.isNotEmpty()) {
            val (heat, x) = queue.poll()
            if (x.point == end) return heat
            if (x in seen) continue
            seen += x

            for (delta in setOf(P.UP, P.DOWN, P.LEFT, P.RIGHT) - setOf(x.delta, x.delta.opposite())) {
                var p = x.point
                var h = heat

                for (i in 1..most) {
                    p += delta
                    if (p.row in board.indices && p.col in board[0].indices) {
                        h += board[p.row][p.col]
                        if (i >= least) queue.add(h to Move(p, delta))
                    }
                }
            }
        }

        throw IllegalStateException("Should always return")
    }

    private fun parse(lines: Sequence<String>) =
        lines.map { line -> line.toList().map { it.toString().toInt() } }.toList()
}

data class P(val row: Int, val col: Int) : Comparable<P> {
    fun opposite() = P(-row, -col)
    operator fun plus(delta: P) = P(row + delta.row, col + delta.col)
    override fun compareTo(other: P) = comparator.compare(this, other)

    companion object {
        val comparator = compareBy<P> { it.row }.thenBy { it.col }
        val UP = P(-1, 0)
        val DOWN = P(1, 0)
        val LEFT = P(0, -1)
        val RIGHT = P(0, 1)
    }
}

data class Move(val point: P, val delta: P)