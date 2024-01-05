package day22

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec : StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 5
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 7
    }

    "processes" {
        Task.process("""
            0,0,1~2,0,1
            0,2,1~2,2,1
            0,4,1~2,4,1
            1,0,2~1,2,2
            1,1,3~1,1,4
        """.trimIndent().split("\n").asSequence()) shouldBe 4
    }

    "processes2a" {
        Task.process2("""
            0,0,1~2,0,1
            0,2,1~2,2,1
            0,4,1~2,4,1
            1,0,2~1,2,2
            1,1,3~1,1,4
        """.trimIndent().split("\n").asSequence()) shouldBe 1
    }

    "processes2b" {
        Task.process2("""
            0,0,1~2,0,1
            0,0,2~0,2,2
            2,0,2~2,2,2
            0,1,3~0,1,3
            0,2,3~0,2,3
            2,1,3~2,1,3
            2,2,3~2,2,3
        """.trimIndent().split("\n").asSequence()) shouldBe 10
    }

    "processes2c" {
        Task.process2("""
            0,0,1~2,0,1
            0,0,2~0,0,3
            2,0,2~2,0,2
            2,0,3~2,0,3
            0,0,4~2,0,4
        """.trimIndent().split("\n").asSequence()) shouldBe 5
    }

    listOf(
        "1,0,1" to P(1, 0, 1),
        "1,2,1" to P(1, 2, 1),
        "0,0,2" to P(0, 0, 2),
        "2,0,2" to P(2, 0, 2),
    ).forEach { (input, expected) ->
        "$input toP()" {
            input.toP() shouldBe expected
        }
    }

    listOf(
        "1,0,1~1,2,1" to Brick(P(1, 0, 1), P(1, 2, 1)),
        "0,0,2~2,0,2" to Brick(P(0, 0, 2), P(2, 0, 2)),
        "0,2,3~2,2,3" to Brick(P(0, 2, 3), P(2, 2, 3))
    ).forEach { (input, expected) ->
        "$input toBrick()" {
            input.toBrick() shouldBe expected
        }
    }

    listOf(
        Pair(2..4, 3..5) to true,
        Pair(2..4, 4..5) to true,
        Pair(2..4, 1..2) to true,
        Pair(2..4, 1..3) to true,
        Pair(2..4, 2..2) to true,
        Pair(2..4, 2..3) to true,
        Pair(2..4, 3..3) to true,
        Pair(2..4, 4..4) to true,
        Pair(2..4, 0..1) to false,
        Pair(2..4, 5..7) to false,
    ).forEach { (ranges, expected) ->
        ranges.let { (a, b) ->
            "$a intersects $b" {
                a intersects b shouldBe expected
            }
        }
    }
})