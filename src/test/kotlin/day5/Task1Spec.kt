package day5

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Task1Spec: StringSpec({

    "parses input" {
        Task1.process(sequenceOf(
            "seeds: 79 14 55 13",
            "",
            "seed-to-soil map:",
            "50 98 2",
            "52 50 48",
            "",
            "soil-to-fertilizer map:",
            "0 15 37",
            "37 52 2",
            "39 0 15",
        ))
    }

    "solves sample" {
        Task1.solve("sample.txt") shouldBe 35
    }
})

/*



79 -> s2s[79] = 81 -> 81

 */