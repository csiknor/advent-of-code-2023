package day17

import java.io.File
import java.util.*

object Task {
    /*
    The solution consists of parsing the input into a matrix, that is used to represent a graph: each item in the matrix
    is a vertex of the graph, connected to vertices next to it in the matrix; the edges are directed and have weight as
    well, which is the value stored in the matrix at the destination vertex. What we need is finding the shortest path
    from the top left to the bottom right corner. We use Dijkstra's algorithm to find the shortest path. Given we have
    to change direction after 3 steps, when we find the next steps, we include all three and choose a different
    direction from there.
     */
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(parse(line)) }

    /*
    The second part is the same as the first, but instead of minimum 1 and max 3 consecutive steps we use minimum 4 and
    max 10 steps.
     */
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(parse(line), 4, 10) }

    fun process(graph: List<List<Int>>, least: Int = 1, most: Int = 3) =
        minHeat(graph, P(0, 0), P(graph.lastIndex, graph[0].lastIndex), least, most)

    fun minHeat(board: List<List<Int>>, start: P, end: P, least: Int, most: Int) =
        generateSequence(priorityQueueOf(0 to Move(start, P.STILL)) to mutableSetOf<Move>()) { (queue, seen) ->
            queue.poll()
                .takeIf { (_, x) -> x !in seen }
                ?.let { (heat, x) ->
                    queue.apply { addAll(nextMoves(x, heat, board, most, least)) } to seen.apply { add(x) }
                }
                ?: (queue to seen)
        }
            .map { (queue, _) -> queue }
            .takeWhile { it.isNotEmpty() }
            .map { it.peek() }
            .firstOrNull { (_, x) -> x.point == end }
            ?.first

    private fun priorityQueueOf(value: Pair<Int, Move>) =
        PriorityQueue(compareBy<Pair<Int, Move>> { it.first }.thenBy { it.second.point }).apply { add(value) }

    private fun nextMoves(x: Move, heat: Int, board: List<List<Int>>, most: Int, least: Int) =
        (setOf(P.UP, P.DOWN, P.LEFT, P.RIGHT) - setOf(x.delta, x.delta.opposite()))
            .fold(mutableSetOf<Pair<Int, Move>>()) { q, delta ->
                generateSequence(heat to x.point) { (h, p) ->
                    (p + delta).takeIf { it.row in board.indices && it.col in board[0].indices }
                        ?.let { (h + board[it.row][it.col]) to it }
                }.drop(1)
                    .take(most)
                    .drop(least - 1)
                    .map { (h, p) -> h to Move(p, delta) }
                    .toCollection(q)
            }

    fun parse(lines: Sequence<String>) =
        lines.map { line -> line.toList().map { it.toString().toInt() } }.toList()
}

data class P(val row: Int, val col: Int) : Comparable<P> {
    fun opposite() = P(-row, -col)
    operator fun plus(delta: P) = P(row + delta.row, col + delta.col)
    operator fun times(value: Int) = P(row * value, col * value)
    override fun compareTo(other: P) = comparator.compare(this, other)

    companion object {
        val comparator = compareBy<P> { it.row }.thenBy { it.col }
        val STILL = P(0, 0)
        val UP = P(-1, 0)
        val DOWN = P(1, 0)
        val LEFT = P(0, -1)
        val RIGHT = P(0, 1)
    }
}

data class Move(val point: P, val delta: P)