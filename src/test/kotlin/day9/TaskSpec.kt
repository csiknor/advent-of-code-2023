package day9

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 114
    }

    "generates differences correctly" {
        generateDifferences(listOf(listOf(0L, 3L, 6L))) shouldBe listOf(
            listOf(0L, 3L, 6L),
            listOf(3L, 3L),
            listOf(0L),
        )
    }

    "generates differences correctly 2" {
        generateDifferences(listOf(listOf(6L, 2L, -1L, -3L, -4L))) shouldBe listOf(
            listOf(6L, 2L, -1L, -3L, -4L),
            listOf(-4L, -3L, -2L, -1L),
            listOf(1L, 1L, 1L),
            listOf(0L, 0L),
        )
    }

    "predicts last" {
        predictLast(
            listOf(
                listOf(9L, 12L, 15L),
                listOf(3L, 3L),
                listOf(0L),
            )
        ) shouldBe 18L
    }

    listOf(
        listOf(
            listOf(0L, 3L, 6L),
            listOf(3L, 3L),
            listOf(0L),
        ) to -3L,
        listOf(
            listOf(1L, 3L, 6L, 10L, 15L),
            listOf(2L, 3L, 4L),
            listOf(1L, 1L),
            listOf(0L),
        ) to 0L,
    ).forEach { (series, expected) ->
        "predicts first $expected" {
            predictFirst(series) shouldBe expected
        }
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 2
    }
})