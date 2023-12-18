package day10

import day10.Direction.*
import java.io.File

enum class Direction { NORTH, EAST, SOUTH, WEST;
    fun opposite() = when(this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }
}

data class Point(val row: Int, val column: Int, val tile: Tile) {

    // Gives the adjacent tile towards the direction we advanced to and the direction we came from.
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

    // The solution involved parsing the input into a matrix of tiles, finding the starting point and identifying the
    // tile at that point, and finally traversing the path from the starting point counting the steps and halving it to
    // get the result.
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(parse(line)) }

    // To find the inner tiles of the path we extract the coordinates of the tiles on the path and for each row we count
    // the tiles between two tiles of the path crossing that row. This involves understanding which side of a path tile
    // is inside or outside. For each row we start from left to right, and if we hit a path tile we flip the side. When
    // the path is horizontally matching the row for a period, we identify if it crosses it eventually or returns to
    // where it came from in which case we don't flip sides.
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process2(parse(line)) }

    fun process(map: MutableMap<Int, MutableMap<Int, Tile>>) = extractStart(map).let { (start, map) ->
        pathFrom(start, map).count() / 2 }

    fun process2(map: MutableMap<Int, MutableMap<Int, Tile>>) = extractStart(map).let { (start, map) ->
        associateByRowAndColumn(pathFrom(start, map))
            .entries.sortedBy { it.key }.map { it.value }.sumOf { row ->
                groundInside(row.entries.sortedBy { it.key })
            }
    }

    // For each row we start from left to right, and if we hit a path tile we flip the side. When
    // the path is horizontally matching the row for a period, we identify if it crosses it eventually or returns to
    // where it came from in which case we don't flip sides.
    private fun groundInside(points: List<Map.Entry<Int, Point>>) = points
        .runningFold(Progress(0, false, null, null)) { prev, (_, point) ->
            val from = if (horizontalLine(point.tile)) prev.from else verticalDirection(point.tile)
            val inside = if (verticalLine(point.tile, prev.from, from)) !prev.inside else prev.inside
            val count = if (prev.inside) point.column - prev.point!!.column - 1 else 0
            Progress(count, inside, from, point)
        }
        .drop(1)
        .sumOf { it.count.toLong() }

    private fun horizontalLine(tile: Tile) = tile == Tile.EW

    private fun verticalLine(tile: Tile, prevFrom: Direction?, from: Direction?) =
        tile == Tile.NS || lineEnd(tile) && prevFrom != from

    private fun verticalDirection(tile: Tile) = tile.neighbours.first { it == NORTH || it == SOUTH }

    private fun lineEnd(tile: Tile) = tile !in listOf(Tile.EW, Tile.NE, Tile.SE)

    private fun associateByRowAndColumn(sequence: Sequence<Point>) = sequence
        .groupBy { it.row }
        .mapValues { (_, points) -> points.associateBy { it.column } }

    // Finding the path is generating a sequence of tile from the starting point advancing towards its next neighbour
    // until we encounter the starting tile again. Each tile has two neighbours, so we need to keep track of the
    // direction we've come from to find the right one to advance towards to.
    private fun pathFrom(start: Point, map: Map<Int, Map<Int, Tile>>) = sequenceOf(start) +
            generateSequence(start.tile.neighbours.last() to start) { (from, point) ->
                point.advanceTowards(point.tile.neighbours.first { it != from }) { row, col -> map.getValue(row).getValue(col) }
            }
                .drop(1)
                .map { (_, point) -> point }
                .takeWhile { it != start }
}

// Extracting the start point involves two steps: finding and identifying the start point and replacing it in the
// matrix.
fun extractStart(map: MutableMap<Int, MutableMap<Int, Tile>>) = startingPoint(map).let { start ->
    start to map.apply { computeIfAbsent(start.row){ mutableMapOf() }[start.column] = start.tile }
}

fun <E> List<E>.replaceElementAt(i: Int, e: E) = take(i).plusElement(e).plus(drop(i+1))

// After finding the start point by traversing the matrix we need to figure out what tile it should be based on its
// neighbours.
fun startingPoint(map: Map<Int, Map<Int, Tile>>) = map.entries.first { (_, row) -> row.values.contains(Tile.S) }
    .let { (r, row) -> r to row.entries.first { (_, tile) -> tile == Tile.S }.key }
    .let { (row, col) -> Point(row, col, Tile.S) }
    .let { it.copy(tile = figureTile(Direction.entries.map { dir -> tileOrNull(it, dir, map) })) }

// Figuring out the tile is based on the adjacent tiles connections towards the starting tile.
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

fun tileOrNull(point: Point, dir: Direction, map: Map<Int, Map<Int, Tile>>) =
    point.advanceTowards(dir) { row, col -> map[row]?.get(col) ?: Tile.G }.second.tile

fun parse(lines: Sequence<String>) = lines
    .mapIndexed { r, line ->
        r to line.mapIndexed { c, char -> c to Tile.byChar(char)!! }
            .filter { (_, tile) -> tile != Tile.G }
            .toMap(mutableMapOf<Int, Tile>().withDefault { Tile.G })
    }
    .toMap(mutableMapOf())
