package day10

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec : StringSpec({
    "solves sample" {
        Task.solvePart1("sample.txt") shouldBe 4
    }
    "solves sample2" {
        Task.solvePart1("sample2.txt") shouldBe 8
    }

    "solves sampleB1" {
        Task.solvePart2("sampleB1.txt") shouldBe 4
    }

    "solves sampleB5" {
        Task.solvePart2("sampleB5.txt") shouldBe 4
    }

    "solves sampleB2" {
        Task.solvePart2("sampleB2.txt") shouldBe 4
    }

    "solves sampleB3" {
        Task.solvePart2("sampleB3.txt") shouldBe 8
    }

    "solves sampleB4" {
        Task.solvePart2("sampleB4.txt") shouldBe 10
    }

    listOf(
        sequenceOf(
            "...",
            ".S-",
            ".|.",
        ) to Point(1, 1, Tile.SE),
        sequenceOf(
            "...",
            "-S.",
            ".|.",
        ) to Point(1, 1, Tile.SW),
        sequenceOf(
            ".|.",
            ".S-",
            "...",
        ) to Point(1, 1, Tile.NE),
        sequenceOf(
            ".|.",
            "-S.",
            "...",
        ) to Point(1, 1, Tile.NW),
        sequenceOf(
            "...",
            "-S-",
            "...",
        ) to Point(1, 1, Tile.EW),
        sequenceOf(
            ".|.",
            ".S.",
            ".|.",
        ) to Point(1, 1, Tile.NS),
        sequenceOf(
            ".S-",
            ".|.",
        ) to Point(0, 1, Tile.SE),
        sequenceOf(
            "..",
            "S-",
            "|.",
        ) to Point(1, 0, Tile.SE),
        sequenceOf(
            "...",
            ".S7",
            ".LJ",
        ) to Point(1, 1, Tile.SE),
        sequenceOf(
            ".-.",
            "7S7",
            ".LJ",
        ) to Point(1, 1, Tile.SE),
    ).forEach { (lines, expected) -> "starting point $expected" { startingPoint(parse(lines)) shouldBe expected } }
})