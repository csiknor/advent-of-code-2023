package day12

import day10.replaceElementAt
import java.io.File

data class Row(val damaged: List<Boolean?>, val counts: List<Int>)

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = parse(lines).map {
        arrangements(it).count()
    }.sum()

    fun arrangements(row: Row): List<Row> {
        if (row.damaged.all { it != null }) return if (valid(row)) listOf(row) else emptyList()

        if (can(row)) {
            val index = row.damaged.indexOfFirst { it == null }
            return listOf(
                arrangements(Row(row.damaged.replaceElementAt(index, true), row.counts)),
                arrangements(Row(row.damaged.replaceElementAt(index, false), row.counts))
            ).flatten()
        }

        return emptyList()
    }

    fun can(row: Row) =
        row.damaged.takeWhile { it != null }.fold(listOf(0)) { acc, it ->
            if (it == true) acc.dropLast(1) + (acc.last()+1)
            else if (acc.last()==0) acc else acc + 0
        }.zip(row.counts).all { (a, b) -> a<=b }

    fun valid(row: Row) =
        row.damaged.fold(listOf(0)) { acc, it ->
            if (it == true) acc.dropLast(1) + (acc.last()+1)
            else if (acc.last()==0) acc else acc + 0
        }.let { if (it.last() == 0) it.dropLast(1) else it } == row.counts

    fun parse(lines: Sequence<String>) = lines.map { line ->
        line.split(" ").let { (first, second) ->
            Row(
                first.map { when (it) { '.' -> false '#' -> true else -> null } },
                second.split(",").map { it.toInt() }
            )
        }
    }
}