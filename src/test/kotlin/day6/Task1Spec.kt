package day6

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Task1Spec: StringSpec({

    listOf("Distance:  9  40  200" to listOf(9, 40, 200)).forEach { (input, result) ->
        "parses correctly" {
            parseLabeledNumberList(input) shouldBe result
        }
    }

    "calculates travel distance" {
        travelDistance(3, 10) shouldBe 21
    }

    "solves sample" {
        Task1.solve("sample.txt") shouldBe 288
    }
})