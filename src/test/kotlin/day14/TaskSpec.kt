package day14

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 136
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 64
    }

    "roll cycle" {
        val input = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent().split("\n")
        Task.rollCycle(input) shouldBe """
            .....#....
            ....#...O#
            ...OO##...
            .OO#......
            .....OOO#.
            .O#...O#.#
            ....O#....
            ......OOOO
            #...O###..
            #..OO#....
        """.trimIndent().split("\n")

        Task.rollCycle(Task.rollCycle(input)) shouldBe """
            .....#....
            ....#...O#
            .....##...
            ..O#......
            .....OOO#.
            .O#...O#.#
            ....O#...O
            .......OOO
            #..OO###..
            #.OOO#...O
        """.trimIndent().split("\n")

        Task.rollCycle(Task.rollCycle(Task.rollCycle(input))) shouldBe """
            .....#....
            ....#...O#
            .....##...
            ..O#......
            .....OOO#.
            .O#...O#.#
            ....O#...O
            .......OOO
            #...O###.O
            #.OOO#...O
        """.trimIndent().split("\n")
    }

    listOf(
        "O....#...." to "O....#....",
        "..O..#O..O" to "O....#OO..",
        "..O#.#O..O" to "O..#.#OO..",
        "..O#.##..O" to "O..#.##O..",
        "#.O#.##..O" to "#O.#.##O..",
        "#.O#.##.O#" to "#O.#.##O.#",
    ).forEach { (input, expected) ->
        "rolls $input" {
            Task.rollLeft(input) shouldBe expected
        }
    }

    listOf(
        "O....#...." to "....O#....",
        "..O..#O..O" to "....O#..OO",
        "..O#.#O..O" to "..O#.#..OO",
        "..O#.##..O" to "..O#.##..O",
        "#.O#.##..O" to "#.O#.##..O",
        "#.O#.##.O#" to "#.O#.##.O#",
    ).forEach { (input, expected) ->
        "rolls $input" {
            Task.rollRight(input) shouldBe expected
        }
    }

    listOf(
        "O....#...." to 10,
        "O....#OO.." to 17,
        "O..#.#OO.." to 17,
        "O..#.##O.." to 13,
    ).forEach { (input, weight) ->
        "weight rocks $input" {
            Task.weighRocks(input) shouldBe weight
        }
    }

    listOf(
        listOf("ab", "cd") to listOf("ac", "bd"),
        listOf("123", "abc") to listOf("1a", "2b", "3c"),
    ).forEach { (input, expected) ->
        "transposes $input" {
            Task.transpose(input) shouldBe expected
        }
    }

    listOf(
        listOf("ab", "cd") to listOf("ca", "db"),
        listOf("123", "abc") to listOf("a1", "b2", "c3"),
    ).forEach { (input, expected) ->
        "4 transpose $input" {
            Task.transpose(Task.transpose(Task.transpose(Task.transpose(input)))) shouldBe input
        }
    }

    listOf(
        listOf(
            "..#",
            "#.#",
            "...",
        ),
    ).forEach { input ->
        "cycle $input" {
            Task.rollCycle(input) shouldBe input
        }
    }

    listOf(
        listOf(2, 0, 6, 3, 1, 6, 3, 1, 6, 3, 1, 6, 3, 1, 6, 3, 1) to listOf(6, 3, 1),
        listOf(2, 0, 6, 3, 6, 6, 5, 3, 1, 6, 5, 3, 1, 6, 5, 3, 1, 6, 5, 3, 1, 6, 5, 3, 1, 6, 5, 3) to listOf(6, 5, 3, 1),
    ).forEach { (numbers, cycle) ->
        "detect cycle $cycle" {
            Task.detectCycle(numbers)!!.let { (start, length) -> numbers.subList(start, start + length).also { println("Cycle: $start [$length]") } } shouldBe cycle
        }
    }
})