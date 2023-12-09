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

    "predicts last" {
        predictLast(listOf(
            listOf(9L, 12L, 15L),
            listOf(3L, 3L),
            listOf(0L),
        )) shouldBe 18L
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe TODO()
    }
})