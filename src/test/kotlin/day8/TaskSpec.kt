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

    "solves part 2 sample" {
        Task.solvePart2("sample_part2.txt") shouldBe 6
    }

    "start points part 2" {
        startingPointsPart2(listOf("11A", "11B", "11Z", "22A", "22B", "22C", "22Z", "XXX")) shouldBe listOf("11A", "22A")
    }

    listOf(
        listOf(2L, 3L, 6L),
        listOf(5L, 10L, 10L),
        listOf(5L, 5L, 5L),
        listOf(14L, 10L, 70L),
        listOf(15L, 12L, 60L),
        listOf(1578L, 2471L, 3899238L),
        listOf(5998L, 10580L, 31729420L),
    ).forEach { (a, b, expected) ->
        "calculates lcm for $a and $b" {
            lcm(a, b) shouldBe expected
        }
    }
})