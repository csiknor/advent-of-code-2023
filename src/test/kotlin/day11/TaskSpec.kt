package day11

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec : StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 374L
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt", 10L) shouldBe 1030L
    }
    "solves sample 2B" {
        Task.solvePart2("sample.txt", 100L) shouldBe 8410L
    }

    listOf(
        sequenceOf(
            "..#",
            "...",
            ".#.",
        ) to listOf(
            Coord(0L, 2L),
            Coord(2L, 1L),
        ),
        sequenceOf(
            "..#.",
            "....",
            ".#.#",
            "..#.",
        ) to listOf(
            Coord(0L, 2L),
            Coord(2L, 1L),
            Coord(2L, 3L),
            Coord(3L, 2L),
        ),
    ).forEach { (input, expected) ->
        "parses" {
            Task.parse(input).toList() shouldBe expected
        }
    }

    listOf(
        listOf(0L, 1L, 2L) to listOf(0L to 0L, 1L to 1L, 2L to 2L),
        listOf(1L, 2L) to listOf(1L to 2L, 2L to 3L),
        listOf(0L, 2L) to listOf(0L to 0L, 2L to 3L),
        listOf(2L) to listOf(2L to 4L),
    ).forEach { (nums, expected) ->
        "expands nums: $nums" {
            Task.expandNumbers(nums, 2L) shouldBe expected
        }
    }

    listOf(
        listOf(Coord(0L, 0L)) to listOf(Coord(0L, 0L)),
        listOf(Coord(1L, 1L)) to listOf(Coord(2L, 2L)),
        listOf(
            Coord(0L, 0L),
            Coord(1L, 1L),
        ) to listOf(
            Coord(0L, 0L),
            Coord(1L, 1L),
        ),
        listOf(
            Coord(0L, 2L),
            Coord(2L, 1L),
        ) to listOf(
            Coord(0L, 3L),
            Coord(3L, 2L),
        ),
        listOf(
            Coord(0L, 2L),
            Coord(2L, 1L),
            Coord(2L, 3L),
            Coord(3L, 2L),
        ) to listOf(
            Coord(0L, 3L),
            Coord(3L, 2L),
            Coord(3L, 4L),
            Coord(4L, 3L),
        ),
    ).forEach { (coords, expected) ->
        "expands coords: $coords" {
            Task.expand(coords, 2L) shouldBe expected
        }
    }

    "connects each" {
        Task.connectEach(listOf(Coord(1L, 1L), Coord(2L, 2L))) shouldBe setOf(
            setOf(Coord(1L, 1L), Coord(2L, 2L))
        )
    }

})