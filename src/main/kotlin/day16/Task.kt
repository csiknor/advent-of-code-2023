package day16

import day16.Dir.*
import java.io.File
import java.util.Comparator

enum class Tile(
    val char: Char,
    val transform: (Dir) -> List<Dir>,
    val equivalent: (Dir) -> Dir
) {
    EMPTY('.', {
        when (it) {
            UP -> listOf(UP)
            DOWN -> listOf(DOWN)
            LEFT -> listOf(LEFT)
            RIGHT -> listOf(RIGHT)
        }
    }, {
        when (it) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }),
    MIRROR_LEFT('/', {
        when (it) {
            UP -> listOf(RIGHT)
            DOWN -> listOf(LEFT)
            LEFT -> listOf(DOWN)
            RIGHT -> listOf(UP)
        }
    }, {
        when (it) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> UP
            RIGHT -> DOWN
        }
    }),
    MIRROR_RIGHT('\\', {
        when (it) {
            UP -> listOf(LEFT)
            DOWN -> listOf(RIGHT)
            LEFT -> listOf(UP)
            RIGHT -> listOf(DOWN)
        }
    }, {
        when (it) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> DOWN
            RIGHT -> UP
        }
    }),
    SPLITTER_HORIZONTAL('-', {
        when (it) {
            UP -> listOf(LEFT, RIGHT)
            DOWN -> listOf(LEFT, RIGHT)
            LEFT -> listOf(LEFT)
            RIGHT -> listOf(RIGHT)
        }
    }, {
        when (it) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }),
    SPLITTER_VERTICAL('|', {
        when (it) {
            UP -> listOf(UP)
            DOWN -> listOf(DOWN)
            LEFT -> listOf(UP, DOWN)
            RIGHT -> listOf(UP, DOWN)
        }
    }, {
        when (it) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    });

    companion object {
        fun ofChar(char: Char) = entries.first { it.char == char }
    }
}

enum class Dir { UP, DOWN, LEFT, RIGHT }

data class Loc(val row: Int, val col: Int) {
    fun move(dir: Dir) = when (dir) {
        UP -> Loc(row - 1, col)
        DOWN -> Loc(row + 1, col)
        LEFT -> Loc(row, col - 1)
        RIGHT -> Loc(row, col + 1)
    }

    override fun toString() = "($row,$col)"
}

data class Move(val loc: Loc, val dir: Dir) {
    fun next(tile: Tile) =
        tile.transform(dir).map { transformed -> Move(loc.move(transformed), transformed) }.toList()

    override fun toString() = "[$loc $dir]"
}

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    /*
    Board is made of tiles
    We have a list of movements: position and direction
    Movements removed if they leave the board OR had the same before in that position and direction
    Movement energises the tile

    . (0,0) R, []
    . (0,1) R, [(0,0) R]
    \ (0,2) R, [(0,0) R; (0,1) R]
    - (1,2) D, [(0,0) R; (0,1) R; (0,2) R]
    .. (1,1) L, (1,3) R [(0,0) R; (0,1) R; (0,2) R; (1,2) D]

    Equivalent directions per tile
    . (L,R)(U,D)
    - (L,R)(U,D)
    | (L,R)(U,D)
    \ (R,U)(L,D)
    / (R,D)(L,U)

     */
    fun process(lines: Sequence<String>) = parse(lines).let { board ->
        generateSequence(listOf(Move(Loc(0, 0), RIGHT)) to emptyList<Move>()) { (movements, history) ->
            movements
                .flatMap { m ->
                    m.next(board[m.loc.row][m.loc.col])
//                        .also { println("$m -> $it") }
                }
                .filter { m -> onBoard(m, board) && !equivalentMoveExists(m, history, board[m.loc.row][m.loc.col]) }
                .let { it to (history + movements) }
        }
            .takeWhile { (movements, _) -> movements.isNotEmpty() }
            .last()
            .let { (movements, history) ->
                history.plus(movements).also { println("History") }
                    .map { it.loc }.distinct()
                    .also { printHistory(it) }
                    .count()
            }
    }

    fun printHistory(history: List<Loc>) {
        history.sortedWith(Comparator.comparing<Loc?, Int?> { it.row }.thenBy { it.col })
            .fold(null as Loc?) { acc, loc ->
                val prevCol = if (acc == null || acc.row != loc.row) -1 else acc.col
                print("\n".repeat(loc.row - (acc?.row ?: 0)))
                print(".".repeat(loc.col - prevCol - 1))
                print("#")

                loc
            }.also { println() }
    }

    fun equivalentMoveExists(move: Move, history: List<Move>, tile: Tile) = history
        .any { m ->
            m.loc == move.loc
                    && (m.dir == move.dir || tile.equivalent(m.dir) == move.dir)
        }

    fun onBoard(move: Move, board: List<List<Tile>>) =
        board.indices.contains(move.loc.row)
                && board.first().indices.contains(move.loc.col)

    fun parse(lines: Sequence<String>) = lines.map { line ->
        line.map { Tile.ofChar(it) }
    }.toList()

}