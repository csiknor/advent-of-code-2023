package day21

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    listOf(
        0 to 1,
        1 to 2,
        2 to 4,
        3 to 6,
        6 to 16,
        10 to 33,
        20 to 42,
    ).forEach { (steps, count) ->
        "solves sample 1 with $steps steps" {
            Task.solvePart1("sample.txt", steps) shouldBe count
        }
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt", 64) shouldBe 42
    }
})