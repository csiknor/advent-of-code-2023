package day16

import day16.Tile.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 46
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 51
    }

    "parses" {
        Task.parse("""
            ..|
            |-/
            /..
        """.trimIndent().split('\n').asSequence()) shouldBe listOf(
            listOf(EMPTY, EMPTY, SPLITTER_VERTICAL),
            listOf(SPLITTER_VERTICAL, SPLITTER_HORIZONTAL, MIRROR_LEFT),
            listOf(MIRROR_LEFT, EMPTY, EMPTY),
        )
    }
})