package day5

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Task2Spec: StringSpec({

    "solves sample" {
        Task2.solve("sample.txt") shouldBe 46
    }
})
