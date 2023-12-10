package day10

import day10.Direction.*
import java.io.File

enum class Direction { NORTH, EAST, SOUTH, WEST }

data class Point(val row: Int, val column: Int, val tile: Tile) {
    fun advanceTowards(it: Direction, lookup: (row: Int, col: Int) -> Tile) = when (it) {
        NORTH -> Triple(SOUTH, row - 1, column)
        EAST -> Triple(WEST, row, column + 1)
        SOUTH -> Triple(NORTH, row + 1, column)
        WEST -> Triple(EAST, row, column - 1)
    }.let { (from, r, c) -> from to Point(r, c, lookup(r, c)) }
}

enum class Tile(val char: Char, val neighbours: List<Direction>) {
    NS('|', listOf(NORTH, SOUTH)),
    EW('-', listOf(WEST, EAST)),
    NE('L', listOf(NORTH, EAST)),
    NW('J', listOf(NORTH, WEST)),
    SW('7', listOf(SOUTH, WEST)),
    SE('F', listOf(SOUTH, EAST)),
    G('.', emptyList()),
    S('S', emptyList());

    companion object {
        fun byChar(c: Char) = entries.find { it.char == c }
    }
}

data class Progress(
    val count: Int,
    val inside: Boolean,
    val from: Direction?,
    val point: Point?
)

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(line) }

    private fun process(lines: Sequence<String>) = extractStart(parse(lines)).let { (start, map) ->
        pathFrom(start, map).count() / 2 }

    private fun process2(lines: Sequence<String>) = extractStart(parse(lines)).let { (start, map) ->
        associateByRowAndColumn(pathFrom(start, map))
            .entries.sortedBy { it.key }.map { it.value }.sumOf { row ->
                groundInside(row.entries.sortedBy { it.key })
            }
    }

    private fun groundInside(points: List<Map.Entry<Int, Point>>) = points
        .runningFold(Progress(0, false, null, null)) { prev, (_, point) ->
            val from = if (horizontalLine(point.tile)) prev.from else verticalDirection(point.tile)
            val inside = if (verticalLine(point.tile, prev.from, from)) !prev.inside else prev.inside
            val count = if (prev.inside) point.column - prev.point!!.column - 1 else 0
            Progress(count, inside, from, point)
        }
        .drop(1)
        .sumOf { it.count }

    private fun horizontalLine(tile: Tile) = tile == Tile.EW

    private fun verticalLine(tile: Tile, prevFrom: Direction?, from: Direction?) =
        tile == Tile.NS || lineEnd(tile) && prevFrom != from

    private fun verticalDirection(tile: Tile) = tile.neighbours.first { it == NORTH || it == SOUTH }

    private fun lineEnd(tile: Tile) = tile !in listOf(Tile.EW, Tile.NE, Tile.SE)

    private fun associateByRowAndColumn(sequence: Sequence<Point>) = sequence
        .groupBy { it.row }
        .mapValues { (_, points) -> points.associateBy { it.column } }

    private fun pathFrom(start: Point, map: List<List<Tile>>) = sequenceOf(start) +
            generateSequence(start.tile.neighbours.last() to start) { (from, point) ->
                point.advanceTowards(point.tile.neighbours.first { it != from }) { row, col -> map[row][col] }
            }
                .drop(1)
                .map { (_, point) -> point }
                .takeWhile { it != start }
}

fun extractStart(map: List<List<Tile>>) = startingPoint(map).let {
    it to map.replaceElementAt(it.row, map[it.row].replaceElementAt(it.column, it.tile))
}

fun <E> List<E>.replaceElementAt(i: Int, e: E) = take(i).plusElement(e).plus(drop(i+1))

fun startingPoint(map: List<List<Tile>>) = map.indexOfFirst { row -> row.contains(Tile.S) }
    .let { it to map[it].indexOfFirst { tile -> tile == Tile.S } }
    .let { (row, col) -> Point(row, col, Tile.S) }
    .let { it.copy(tile = figureTile(Direction.entries.map { dir -> tileOrNull(it, dir, map) })) }

fun figureTile(adjacent: List<Tile>) = when {
    adjacent.component1().neighbours.contains(SOUTH) -> when {
        adjacent.component2().neighbours.contains(WEST) -> Tile.NE
        adjacent.component3().neighbours.contains(NORTH) -> Tile.NS
        adjacent.component4().neighbours.contains(EAST) -> Tile.NW
        else -> throw Error("North-???")
    }
    adjacent.component3().neighbours.contains(NORTH) -> when {
        adjacent.component2().neighbours.contains(WEST) -> Tile.SE
        adjacent.component1().neighbours.contains(SOUTH) -> Tile.NS
        adjacent.component4().neighbours.contains(EAST) -> Tile.SW
        else -> throw Error("South-???")
    }
    else -> Tile.EW
}

fun tileOrNull(point: Point, dir: Direction, map: List<List<Tile>>) =
    point.advanceTowards(dir) { row, col -> map.getOrNull(row)?.getOrNull(col) ?: Tile.G }.second.tile

fun parse(lines: Sequence<String>) = lines
    .map { line -> line.map { Tile.byChar(it)!! } }
    .toList()
