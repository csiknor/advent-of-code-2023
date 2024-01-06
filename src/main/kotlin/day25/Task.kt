package day25

import java.io.File
import java.util.LinkedList
import java.util.PriorityQueue

object Task {

    /*
    The solution builds on the idea that these three edges we are looking for must be on the shortest path from the two
    graphs they're separate. Of course, it is not a generic solution, but works for the input.
    Therefore, after parsing the input into a graph, we try each and every edge and calculate the shortest past between
    the two vertices of the edge if the edge is removed from the graph. Once we find the top 3 edges causing the biggest
    changes in the shortest paths, we remove them, and calculate how many vertices can be reached from an arbitrary
    vertex and compare it to the number of all vertices to calculate the difference and get the result.
     */
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = parse(lines).let { graph ->
        graph.asSequence().flatMap { (source, targets) -> targets.map { target -> setOf(source, target) } }
            .toSet()
            .map { edge -> edge to shortestPathSize(graph.removeEdge(edge), edge.first(), edge.last()) }
            .sortedByDescending { it.second }
            .take(3)
            .fold(graph) { acc, (edge, _) -> acc.removeEdge(edge) }
            .let { split -> reachable(split, split.keys.first()).let { (split.count() - it) * it } }
    }

    // Breadth-first traversal
    private fun reachable(graph: Map<String, Set<String>>, vertex: String) =
        generateSequence(
            LinkedList<String>().apply { offer(vertex) } to mutableSetOf(vertex)
        ) { (queue, seen) ->
            queue.poll().let { v ->
                graph.getValue(v)
                    .filter { it !in seen }
                    .fold(queue to seen) { (q, s), w -> q.apply { offer(w) } to s.apply { add(w) } }
            }
        }
            .takeWhile { (queue, _) -> queue.isNotEmpty() }
            .last().second.count()

    private fun Map<String, Set<String>>.removeEdge(edge: Set<String>) = toMutableMap().apply {
        compute(edge.first()) { _, value -> value?.minus(edge.last())?.takeIf { it.isNotEmpty() } }
        compute(edge.last()) { _, value -> value?.minus(edge.first())?.takeIf { it.isNotEmpty() } }
    }

    // Dijkstra's algorithm
    fun shortestPathSize(graph: Map<String, Set<String>>, source: String, target: String) =
        generateSequence(
            priorityQueueOf(0 to source) to startingDistances(graph.keys, source)
        ) { (queue, dist) ->
            queue.poll().takeIf { (k, u) -> k == dist[u] }?.let { (_, u) ->
                graph.getValue(u)
                    .filter { v -> dist.getValue(v) > dist.getValue(u) + 1 }
                    .fold(queue to dist) { (q, d), v ->
                        q.apply { add(dist.getValue(u) + 1 to v) } to d.apply { set(v, dist.getValue(u) + 1) }
                    }
            }
        }
            .takeWhile { (queue, _) -> queue.isNotEmpty() }
            .firstOrNull { (queue, _) -> queue.peek().second == target }
            ?.second?.getValue(target)
            ?: error("Shortest path couldn't be found")

    private fun startingDistances(vertices: Set<String>, source: String) =
        vertices.associateWithTo(mutableMapOf()) { if (it == source) 0 else Int.MAX_VALUE }

    private fun priorityQueueOf(value: Pair<Int, String>) =
        PriorityQueue<Pair<Int, String>>(compareBy { it.first }).apply { add(value) }

    fun parse(lines: Sequence<String>) = lines.flatMap { line ->
        line.split(": ").let { (source, targets) ->
            targets.split(" ").flatMap { target -> setOf(source to target, target to source) }
        }
    }
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.toSet() }

}