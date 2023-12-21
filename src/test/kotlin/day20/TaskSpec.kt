package day20

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec : StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 32000000
    }

    "solves sample 1/2" {
        Task.solvePart1("sample2.txt") shouldBe 11687500
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 4
    }
})