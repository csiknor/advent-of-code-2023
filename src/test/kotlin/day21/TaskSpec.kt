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

    listOf(
        6 to 16,
        10 to 50,
        50 to 1594,
        100 to 6536,
        500 to 167004,
        1000 to 668697,
        5000 to 16733044,
    ).forEach { (steps, count) ->
        "solves sample 2 with $steps steps" {
            Task.solvePart2("sample.txt", steps) shouldBe count
        }
    }

    "solves sample 2 with a lot of steps" {
        Task.solvePart2("sample.txt", 50) shouldBe 1594
    }

})