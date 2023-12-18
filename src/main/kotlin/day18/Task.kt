package day18

import day10.Direction
import day10.Task
import day10.Tile
import java.io.File

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(parse(line)) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(parse(line)) }

    fun process(digs: Sequence<Dig>) =
        digs
            .fold(
                Triple(
                    P(0, 0),
                    sortedMapOf<Int, MutableMap<Int, Tile>>(),
                    null as Direction?
                )
            ) { (p, map, prev), dig ->
                val curr = when (dig.dir) {
                    Direction.NORTH -> digger(map, p, prev, dig, Direction.NORTH, Tile.NS, P(-1, 0))
                    Direction.EAST -> digger(map, p, prev, dig, Direction.EAST, Tile.EW, P(0, 1))
                    Direction.SOUTH -> digger(map, p, prev, dig, Direction.SOUTH, Tile.NS, P(1, 0))
                    Direction.WEST -> digger(map, p, prev, dig, Direction.WEST, Tile.EW, P(0, -1))
                }

                Triple(curr, map, dig.dir.opposite())
            }
            .also { (p, map, _) -> map.getValue(p.row)[p.col] = Tile.S }
            .let { (p, map, _) ->
                Task.process(map).toLong() * 2 + Task.process2(map.apply { getValue(p.row)[p.col] = Tile.S })
            }

    private fun digger(
        map: MutableMap<Int, MutableMap<Int, Tile>>,
        p: P,
        prev: Direction?,
        dig: Dig,
        direction: Direction,
        tile: Tile,
        move: P,
    ): P {
        map.computeIfAbsent(p.row) { sortedMapOf() }[p.col] =
            if (prev == null) tile else Tile.entries.find { it.neighbours.toSet() == setOf(direction, prev) }!!
        1.rangeTo(dig.count)
            .forEach { i -> map.computeIfAbsent(p.row + move.row * i) { sortedMapOf() }[p.col + move.col * i] = tile }
        return P(p.row + move.row * dig.count, p.col + move.col * dig.count)
    }

    private fun parse(lines: Sequence<String>) = lines.map { line ->
        line.split(" ").let { (dir, count, color) ->
            Dig(Dir.valueOf(dir).d, count.toInt(), color.substring(1, color.lastIndex))
        }
    }
}

data class P(val row: Int, val col: Int)

enum class Dir(val d: Direction) {
    R(Direction.EAST),
    D(Direction.SOUTH),
    L(Direction.WEST),
    U(Direction.NORTH),
}

data class Dig(val dir: Direction, val count: Int, val color: String? = null)