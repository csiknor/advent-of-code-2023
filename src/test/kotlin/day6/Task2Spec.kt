package day6

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Task2Spec: StringSpec({

    listOf("Distance:  9  40  200" to 940200).forEach { (input, result) ->
        "parses correctly" {
            parseLabeledNumberListWithBadKerning(input) shouldBe result
        }
    }

    "solves sample" {
        Task2.solve("sample.txt") shouldBe 71503
    }
})