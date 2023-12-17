package day17

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 102
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 94
    }

    "test" {
        Task.process("""
            123
            456
            789
        """.trimIndent().split("\n").asSequence()).let { println("Result: $it") }
    }
})