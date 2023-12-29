package day18

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 62
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 952408144115
    }
})