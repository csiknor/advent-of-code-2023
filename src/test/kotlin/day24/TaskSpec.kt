package day24

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec : StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt", 7.0, 27.0) shouldBe 2
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 47
    }

    listOf(
        Pair("19, 13, 30 @ -2, 1, -2", "18, 19, 22 @ -1, -1, -2") to P(x = 14.333, y = 15.333),
        Pair("19, 13, 30 @ -2, 1, -2", "20, 25, 34 @ -2, -2, -4") to P(x = 11.667, y = 16.667),
        Pair("19, 13, 30 @ -2, 1, -2", "12, 31, 28 @ -1, -2, -1") to P(x = 6.2, y = 19.4),
        Pair("19, 13, 30 @ -2, 1, -2", "20, 19, 15 @ 1, -5, -3") to null,
        // P(x = 21.444, y = 11.778),
        Pair("18, 19, 22 @ -1, -1, -2", "20, 25, 34 @ -2, -2, -4") to null,
        // P(x = Double.NEGATIVE_INFINITY, y = Double.NEGATIVE_INFINITY),
        Pair("18, 19, 22 @ -1, -1, -2", "12, 31, 28 @ -1, -2, -1") to P(x = -6.0, y = -5.0),
        Pair("18, 19, 22 @ -1, -1, -2", "20, 19, 15 @ 1, -5, -3") to null,
        // x=19.667 y=20.667
        Pair("20, 25, 34 @ -2, -2, -4", "12, 31, 28 @ -1, -2, -1") to P(x=-2.0, y=3.0),
        Pair("20, 25, 34 @ -2, -2, -4", "20, 19, 15 @ 1, -5, -3") to null,
        // x=19.0 y=24.0
        Pair("12, 31, 28 @ -1, -2, -1", "20, 19, 15 @ 1, -5, -3") to null,
        // x=16.0 y=39.0
    ).forEach { (points, expected) ->
        points.let { (a, b) ->
            "$a intersection $b" {
                Task.intersection(a.toPV(),b.toPV())?.rounded() shouldBe expected
            }
        }
    }
})