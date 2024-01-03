package day16

import day16.Dir.*
import java.io.File

object Task {
    /*
    The solution involves parsing the input into a board of tiles, that we use to follow the light entering from the top
    left corner and moving towards right. At each step the light is cast into one or more lights depending on the
    current tile: it might remain the same, change direction or split into two. We follow all of them, unless seen
    before. Finally, we count the tiles on the board that has been visited by the light.
     */
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { lines ->
            parse(lines).let { board ->
                process(board, listOf(Loc(0, 0).toMove(RIGHT, board)))
            }
        }

    /*
    The second part is the same, except, here we start from multiple positions, both from each edges of the horizontal
    and vertical sides of the board.
    Note: for each light the number of energised tiles are calculated independently, which can be slow, depending on the
    size of the input. It could've been improved by introducing some sort of caching.
     */
    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { lines ->
            parse(lines).let { board ->
                process(board, horizontalMoves(board) + verticalMoves(board))
            }
        }

    private fun verticalMoves(board: List<List<Tile>>) =
        board.first().indices.flatMap { colIndex ->
            listOf(
                Loc(0, colIndex).toMove(DOWN, board),
                Loc(board.lastIndex, colIndex).toMove(UP, board)
            )
        }

    private fun horizontalMoves(board: List<List<Tile>>) =
        board.indices.flatMap { rowIndex ->
            listOf(
                Loc(rowIndex, 0).toMove(RIGHT, board),
                Loc(rowIndex, board.first().lastIndex).toMove(LEFT, board)
            )
        }

    fun process(board: List<List<Tile>>, starts: List<Move>) = starts.maxOfOrNull { start ->
        generateSequence(listOf(start) to emptySet<Move>()) { (movements, history) ->
            movements
                .flatMap { it.next(board) }
                .filter { it !in history }
                .let { it to (history + movements) }
        }
            .takeWhile { (movements, _) -> movements.isNotEmpty() }
            .last()
            .let { (movements, history) -> history + movements }
            .map { it.loc }
            .toSet()
            .count()
    }

    fun parse(lines: Sequence<String>) = lines.map { line -> line.map { it.toTile() } }.toList()
}

enum class Tile(
    val char: Char,
    val transform: (Dir) -> List<Dir>
) {
    EMPTY('.', {
        when (it) {
            UP -> listOf(UP)
            DOWN -> listOf(DOWN)
            LEFT -> listOf(LEFT)
            RIGHT -> listOf(RIGHT)
        }
    }),
    MIRROR_LEFT('/', {
        when (it) {
            UP -> listOf(RIGHT)
            DOWN -> listOf(LEFT)
            LEFT -> listOf(DOWN)
            RIGHT -> listOf(UP)
        }
    }),
    MIRROR_RIGHT('\\', {
        when (it) {
            UP -> listOf(LEFT)
            DOWN -> listOf(RIGHT)
            LEFT -> listOf(UP)
            RIGHT -> listOf(DOWN)
        }
    }),
    SPLITTER_HORIZONTAL('-', {
        when (it) {
            UP -> listOf(LEFT, RIGHT)
            DOWN -> listOf(LEFT, RIGHT)
            LEFT -> listOf(LEFT)
            RIGHT -> listOf(RIGHT)
        }
    }),
    SPLITTER_VERTICAL('|', {
        when (it) {
            UP -> listOf(UP)
            DOWN -> listOf(DOWN)
            LEFT -> listOf(UP, DOWN)
            RIGHT -> listOf(UP, DOWN)
        }
    });
}

fun Char.toTile() = Tile.entries.first { it.char == this }

enum class Dir(val delta: Loc) { UP(Loc(-1, 0)), DOWN(Loc(1, 0)), LEFT(Loc(0, -1)), RIGHT(Loc(0, 1)) }

data class Loc(val row: Int, val col: Int) {
    operator fun plus(other: Loc) = Loc(row + other.row, col + other.col)
    override fun toString() = "($row,$col)"
}

fun Loc.toMove(to: Dir, board: List<List<Tile>>) = Move(this, to, board[row][col])

fun Loc.onBoard(board: List<List<Tile>>) = board.indices.contains(row) && board.first().indices.contains(col)

data class Move(val loc: Loc, val to: Dir, val tile: Tile) {
    fun next(board: List<List<Tile>>) = tile.transform(to)
        .map { it to loc + it.delta }
        .filter { (_, loc) -> loc.onBoard(board) }
        .map { (transformed, newLoc) -> Move(newLoc, transformed, board[newLoc.row][newLoc.col]) }

    override fun toString() = "[$loc $to $tile]"
}
