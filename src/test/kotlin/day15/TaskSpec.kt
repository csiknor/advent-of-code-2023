package day15

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 1320
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 145
    }

    "hash" {
        Task.hash("HASH") shouldBe 52
    }
})