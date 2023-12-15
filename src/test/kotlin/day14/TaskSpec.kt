package day14

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 136
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe TODO()
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
        listOf("ab", "cd") to listOf("ac", "bd"),
        listOf("123", "abc") to listOf("1a", "2b", "3c"),
    ).forEach { (input, expected) ->
        "transposes $input" {
            Task.transpose(input) shouldBe expected
        }
    }
})