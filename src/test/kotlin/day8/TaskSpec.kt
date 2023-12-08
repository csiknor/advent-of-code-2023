package day8

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({

    "parses direction" {
        connectionsOf("AAA = (BBB, CCC)") shouldBe ("AAA" to ("BBB" to "CCC"))
    }

    "solves sample 1" {
        Task.solvePart1("sample1.txt") shouldBe 2
    }
    "solves sample 2" {
        Task.solvePart1("sample2.txt") shouldBe 6
    }
})