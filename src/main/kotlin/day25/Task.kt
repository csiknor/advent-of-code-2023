package day25

import java.io.File
import java.util.LinkedList
import java.util.PriorityQueue

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = parse(lines).let { graph ->
        graph.flatMap { (source, targets) -> targets.map { target -> setOf(source, target) } }
            .toSet()
            .map { edge -> edge to dijkstra(graph.removeEdge(edge), edge.first(), edge.last()).size }
            .sortedWith(compareBy<Pair<Set<String>, Int>> { it.second }.reversed())
            .take(3)
            .fold(graph) { acc, (edge, _) ->
                acc.removeEdge(edge)
            }.let { splitted ->
                reachable(splitted, splitted.keys.first()).count().let { (splitted.count() - it) * it }
            }
    }

    private fun reachable(graph: Map<String, Set<String>>, vertex: String): Set<String> {
        val seen = mutableSetOf(vertex)
        val queue = LinkedList<String>()
        queue.offer(vertex)

        while (queue.isNotEmpty()) {
            val v = queue.poll()
            for (w in graph.getValue(v)) {
                if (w !in seen) {
                    seen += w
                    queue.offer(w)
                }
            }
        }

        return seen
    }

    fun Map<String, Set<String>>.removeEdge(edge: Set<String>) = toMutableMap().apply {
        compute(edge.first()) { _, value -> value?.minus(edge.last())?.takeIf { it.isNotEmpty() }}
        compute(edge.last()) { _, value -> value?.minus(edge.first())?.takeIf { it.isNotEmpty() }}
    }

    fun dijkstra(graph: Map<String, Set<String>>, source: String, target: String): List<String> {
        val dist = graph.mapValues { Int.MAX_VALUE }.toMutableMap()
        dist[source] = 0
        val prev = mutableMapOf<String, String>()

        val queue = PriorityQueue<String>(compareBy { dist[it] })
        queue.addAll(graph.keys)

        while (queue.isNotEmpty()) {
            val u = queue.poll()
            if (u == target) break

            for (v in graph[u]!!) {
                val alt = dist[u]!! + 1
                if (alt < dist[v]!!) {
                    dist[v] = alt
                    prev[v] = u
                    queue.remove(v)
                    queue.add(v)
                }
            }
        }

        return generateSequence(target) { prev[it] }.toList().reversed()
    }


    fun parse(lines: Sequence<String>) = lines.flatMap { line ->
        line.split(": ").let { (source, targets) ->
            targets.split(" ").flatMap { target ->
                setOf(source to target, target to source)
            }
        }
    }
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.toSet() }

}